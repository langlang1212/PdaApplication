package com.pda.utils;

import com.alibaba.fastjson.JSONObject;
import com.pda.api.dto.UserResDto;
import com.pda.common.PdaBaseService;
import org.springframework.stereotype.Component;

/**
 * @Classname SecurityUtil
 * @Description TODO
 * @Date 2022-07-31 22:05
 * @Created by AlanZhang
 */
@Component
public class SecurityUtil extends PdaBaseService {

    public UserResDto getCurrentUser(){
        return JSONObject.parseObject((String) getSession().getAttribute("user")).toJavaObject(UserResDto.class);
    }
}
