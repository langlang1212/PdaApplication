package com.pda.api.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.XmlUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pda.api.domain.entity.ViewPassword;
import com.pda.api.domain.service.IViewPasswordService;
import com.pda.api.dto.UserResDto;
import com.pda.api.service.AsyncService;
import com.pda.api.service.DeptService;
import com.pda.api.service.LoginService;
import com.pda.api.service.UserService;
import com.pda.common.redis.service.RedisService;
import com.pda.exception.BusinessException;
import com.pda.utils.PdaToJavaObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Classname LoginServiceImpl
 * @Description TODO
 * @Date 2022-07-23 9:50
 * @Created by AlanZhang
 */
@Slf4j
@Service
public class LoginServiceImpl implements LoginService {
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
    @Override
    public UserResDto login(String account, String password) {
        if(ObjectUtils.isEmpty(checkUser(account,password))){
            throw new BusinessException("用户名或密码错误，请检查用户名或密码!");
        }
        List userList = getUsers();
        for(Object obj : userList){
            UserResDto userResDto = JSONObject.parseObject(JSON.toJSONString(obj)).toJavaObject(UserResDto.class);
            if(account.equals(userResDto.getUserName())){
                // 设置病区
                List deptList = getDepts();
                for(Object deptObj : deptList){
                    JSONObject deptJsonObj = JSONObject.parseObject(JSON.toJSONString(deptObj));
                    if(userResDto.getDeptCode().equals(deptJsonObj.getString("DEPT_CODE"))){
                        userResDto.setDeptName(deptJsonObj.getString("DEPT_NAME"));
                    }
                }
                //
                return userResDto;
            }
        }
        return null;
    }

    private List getUsers(){
        List userList = redisService.getCacheList("user_list");
        if(CollectionUtil.isEmpty(userList)){
            String userStr = userService.list(1);
            userList = PdaToJavaObjectUtil.convertList(userStr);
            // 存入redis
            asyncService.saveList("user_list",userList);
        }
        return userList;
    }

    private List getDepts(){
        List deptList = redisService.getCacheList("dept_list");
        if(CollectionUtil.isEmpty(deptList)){
            String deptStr = deptService.list(1);
            deptList = PdaToJavaObjectUtil.convertList(deptStr);
            // 存入redis
            asyncService.saveList("dept_list",deptList);
        }
        return deptList;
    }

    public ViewPassword checkUser(String account,String password){
        LambdaQueryWrapper<ViewPassword> vpWrapper = new LambdaQueryWrapper<ViewPassword>().eq(ViewPassword::getUserName,account).eq(ViewPassword::getPassword,password);
        ViewPassword one = iViewPasswordService.getOne(vpWrapper);
        return one;
    }

}
