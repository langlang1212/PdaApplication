package com.pda.api.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.XmlUtil;
import cn.hutool.crypto.digest.MD5;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pda.api.domain.entity.ViewPassword;
import com.pda.api.domain.mapper.OrdersMMapper;
import com.pda.api.domain.mapper.PatientInfoMapper;
import com.pda.api.domain.service.IViewPasswordService;
import com.pda.api.dto.UserResDto;
import com.pda.api.service.*;
import com.pda.common.Constant;
import com.pda.common.PdaBaseService;
import com.pda.common.dto.DictDto;
import com.pda.common.redis.service.RedisService;
import com.pda.exception.BusinessException;
import com.pda.utils.PdaToJavaObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Classname LoginServiceImpl
 * @Description TODO
 * @Date 2022-07-23 9:50
 * @Created by AlanZhang
 */
@Slf4j
@Service
public class LoginServiceImpl extends PdaBaseService implements LoginService {
    @Autowired
    private UserService userService;
    @Autowired
    private AsyncService asyncService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private IViewPasswordService iViewPasswordService;
    @Autowired
    private DeptService deptService;
    @Autowired
    private OrdersMMapper ordersMMapper;
    @Autowired
    private PatientInfoMapper patientInfoMapper;
    @Autowired
    private PdaService pdaService;
    @Override
    public UserResDto login(String account, String password) {
        if(ObjectUtils.isEmpty(checkUser(account,password))){
            throw new BusinessException("用户名或密码错误，请检查用户名或密码!");
        }
        List userList = pdaService.getUsers();
        for(Object obj : userList){
            UserResDto userResDto = JSONObject.parseObject(JSON.toJSONString(obj)).toJavaObject(UserResDto.class);
            if(account.equals(userResDto.getUserName())){
                // 设置病区
                List deptList = pdaService.getDepts();
                for(Object deptObj : deptList){
                    JSONObject deptJsonObj = JSONObject.parseObject(JSON.toJSONString(deptObj));
                    if(userResDto.getDeptCode().equals(deptJsonObj.getString("DEPT_CODE"))){
                        userResDto.setDeptName(deptJsonObj.getString("DEPT_NAME"));
                    }
                }
                // 设置病区列表
                setWards(userResDto);
                // 存储当前用户
                // TODO: 2022-07-31 后续改造成使用token redis存储，暂时用session存储 
                getSession().setAttribute("user",JSON.toJSONString(userResDto));
                return userResDto;
            }
        }
        return null;
    }

    private void setWards(UserResDto userResDto) {
        List<DictDto> wards = new ArrayList<>();
        if(Constant.DOCTOR.equals(userResDto.getJob())){ //医生
            wards = patientInfoMapper.selectWardByPatient(userResDto.getUserName());
        }else if(Constant.NURSE.equals(userResDto.getJob())){ //护士
            wards = ordersMMapper.selectWardByOrder(userResDto.getUserName());
        }
        userResDto.setWards(wards);
    }

    public ViewPassword checkUser(String account,String password){
        LambdaQueryWrapper<ViewPassword> vpWrapper = new LambdaQueryWrapper<ViewPassword>().eq(ViewPassword::getUserName,account).eq(ViewPassword::getPassword,password);
        ViewPassword one = iViewPasswordService.getOne(vpWrapper);
        return one;
    }

}
