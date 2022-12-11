package com.pda.api.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pda.api.domain.entity.ViewPassword;
import com.pda.api.domain.service.IViewPasswordService;
import com.pda.api.dto.UserResDto;
import com.pda.api.mapper.primary.MobileCommonMapper;
import com.pda.api.mapper.primary.OrdersMMapper;
import com.pda.api.mapper.primary.PatientInfoMapper;
import com.pda.api.mapper.primary.ViewPasswordMapper;
import com.pda.api.service.*;
import com.pda.common.Constant;
import com.pda.common.PdaBaseService;
import com.pda.common.dto.DictDto;
import com.pda.common.redis.service.RedisService;
import com.pda.exception.BusinessException;
import com.pda.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
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
    private IViewPasswordService iViewPasswordService;
    @Autowired
    private MobileCommonMapper mobileCommonMapper;
    @Autowired
    private RedisService redisService;

    @Value("${cache.loginToken}")
    private Long invalidDuration;

    @Override
    public Map<String,Object> login(String account, String password) {
        if(ObjectUtils.isEmpty(checkUser(account,password))){
            throw new BusinessException("用户名或密码错误，请检查用户名或密码!");
        }
        /*List userList = pdaService.getUsers();
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
                Map<String,Object> map = new HashMap<>();
                String key = DigestUtil.md5Hex(userResDto.getUserName()+":"+ DateUtil.getShortDate(new Date()), "utf-8");
                map.put("accessToken",key);
                map.put("user",userResDto);
                // 放入redis 有效期1天
                redisService.setCacheObject(key,userResDto,invalidDuration, TimeUnit.DAYS);
                return map;
            }
        }*/
        // 1、查询用户信息
        UserResDto userResDto = mobileCommonMapper.checkUser(account);
        // 2、设置病区
        setWards(userResDto);
        // 存储当前用户
        Map<String,Object> map = new HashMap<>();
        String key = DigestUtil.md5Hex(userResDto.getUserName()+":"+ DateUtil.getShortDate(new Date()), "utf-8");
        map.put("accessToken",key);
        map.put("user",userResDto);
        // 放入redis 有效期1天
        redisService.setCacheObject(key,userResDto,invalidDuration, TimeUnit.DAYS);
        return map;
    }

    @Override
    public void logout(String accessToken) {
        redisService.deleteObject(accessToken);
    }

    @Override
    public Map<String, Object> loginByQrcode(String account) {
        // 1、查询用户信息
        UserResDto userResDto = mobileCommonMapper.checkUser(account);
        // 2、设置病区
        setWards(userResDto);
        // 存储当前用户
        Map<String,Object> map = new HashMap<>();
        String key = DigestUtil.md5Hex(userResDto.getUserName()+":"+ DateUtil.getShortDate(new Date()), "utf-8");
        map.put("accessToken",key);
        map.put("user",userResDto);
        // 放入redis 有效期1天
        redisService.setCacheObject(key,userResDto,invalidDuration, TimeUnit.DAYS);
        return map;
    }

    private void setWards(UserResDto userResDto) {
        List<DictDto> wards = new ArrayList<>();
        if(Constant.DOCTOR.equals(userResDto.getJob())){ //医生
            wards = mobileCommonMapper.selectDoctorWard(userResDto.getUserName());
        }else{ //护士
            wards = mobileCommonMapper.selectNurseWard(userResDto.getUserName());
        }
        userResDto.setWards(wards);
    }

    public ViewPassword checkUser(String account,String password){
        LambdaQueryWrapper<ViewPassword> vpWrapper = new LambdaQueryWrapper<ViewPassword>().eq(ViewPassword::getUserName,account).eq(ViewPassword::getPassword,password);
        ViewPassword one = iViewPasswordService.getOne(vpWrapper);
        return one;
    }

}
