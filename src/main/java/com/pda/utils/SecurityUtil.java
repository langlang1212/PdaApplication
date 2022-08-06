package com.pda.utils;

import com.alibaba.fastjson.JSONObject;
import com.pda.api.dto.UserResDto;
import com.pda.common.PdaBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Classname SecurityUtil
 * @Description TODO
 * @Date 2022-07-31 22:05
 * @Created by AlanZhang
 */
public class SecurityUtil {

    private static final ThreadLocal<UserResDto> userThreadLocal = new ThreadLocal<>();

    @Autowired

    /**
     * 添加当前登录用户方法
     */
    public static void addCurrentUser(UserResDto userResDto) {
        userThreadLocal.set(userResDto);
    }

    public static void remove(){
        userThreadLocal.remove();
    }

    public static UserResDto getCurrentUser(){
        return userThreadLocal.get();
    }
}
