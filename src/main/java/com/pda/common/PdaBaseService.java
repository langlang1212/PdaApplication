package com.pda.common;

import com.pda.common.config.WsProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Classname PdaBaseService
 * @Description TODO
 * @Date 2022-07-22 15:24
 * @Created by AlanZhang
 */
@Service
public class PdaBaseService {

    @Autowired
    private WsProperties wsProperties;

    public WsProperties getWsProperties(){
        return this.wsProperties;
    }
}
