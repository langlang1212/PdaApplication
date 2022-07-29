package com.pda.api.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.XmlUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pda.api.dto.UserResDto;
import com.pda.api.service.AsyncService;
import com.pda.api.service.LoginService;
import com.pda.api.service.UserService;
import com.pda.common.redis.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    @Override
    public UserResDto login(String account, String password) {
        List userList = redisService.getCacheList("user_list");
        if(CollectionUtil.isEmpty(userList)){
            String userStr = userService.list(1);
            Map<String, Object> stringObjectMap = XmlUtil.xmlToMap(userStr);
            Map<String,Object> controlActMap = (Map<String, Object>) stringObjectMap.get("ControlActProcess");
            userList = (List) controlActMap.get("List");
            // 存入redis
            asyncService.saveUser(userList);
        }
        for(Object obj : userList){
            UserResDto userResDto = JSONObject.parseObject(JSON.toJSONString(obj)).toJavaObject(UserResDto.class);
            if(account.equals(userResDto.getEmpNo())){
                return userResDto;
            }
        }
        return null;
    }
}
