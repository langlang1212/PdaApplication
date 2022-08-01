package com.pda.api.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pda.api.dto.UserResDto;
import com.pda.api.service.AsyncService;
import com.pda.api.service.DeptService;
import com.pda.api.service.PdaService;
import com.pda.api.service.UserService;
import com.pda.common.redis.service.RedisService;
import com.pda.utils.PdaToJavaObjectUtil;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Classname PdaServiceImpl
 * @Description TODO
 * @Date 2022-07-31 20:56
 * @Created by AlanZhang
 */
@Service
public class PdaServiceImpl implements PdaService {
    @Autowired
    private RedisService redisService;
    @Autowired
    private UserService userService;
    @Autowired
    private DeptService deptService;
    @Autowired
    private AsyncService asyncService;
    @Override
    public List getUsers() {
        List userList = redisService.getCacheList("user_list");
        if(CollectionUtil.isEmpty(userList)){
            String userStr = userService.list(1);
            userList = PdaToJavaObjectUtil.convertList(userStr);
            // 存入redis
            asyncService.saveList("user_list",userList);
        }
        return userList;
    }

    @Override
    public List getDepts() {
        List deptList = redisService.getCacheList("dept_list");
        if(CollectionUtil.isEmpty(deptList)){
            String deptStr = deptService.list(1);
            deptList = PdaToJavaObjectUtil.convertList(deptStr);
            // 存入redis
            asyncService.saveList("dept_list",deptList);
        }
        return deptList;
    }

    @Override
    public UserResDto getCurrentUser() {

        return null;
    }

    @Override
    public UserResDto getUserByCode(String userName) {
        List users = getUsers();
        for(Object obj : users) {
            UserResDto userResDto = JSONObject.parseObject(JSON.toJSONString(obj)).toJavaObject(UserResDto.class);
            if(userName.equals(userResDto.getUserName())){
                return userResDto;
            }
        }
        return null;
    }
}
