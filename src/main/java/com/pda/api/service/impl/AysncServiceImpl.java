package com.pda.api.service.impl;

import com.pda.api.service.AsyncService;
import com.pda.common.redis.configure.RedisConfig;
import com.pda.common.redis.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Classname AysncServiceImpl
 * @Description TODO
 * @Date 2022-07-23 11:51
 * @Created by AlanZhang
 */
@Service
public class AysncServiceImpl implements AsyncService {

    @Autowired
    private RedisService redisService;

    @Async
    @Override
    public void saveUser(List userList) {
        redisService.setCacheListExpire("user_list",userList,1l, TimeUnit.DAYS);
    }
}
