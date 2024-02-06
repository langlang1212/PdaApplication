package com.pda.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Classname WsProperties
 * @Description TODO
 * @Date 2022-07-22 12:44
 * @Created by AlanZhang
 */
@Component
@Data
@ConfigurationProperties(prefix = "ws")
public class WsProperties {

    private String forwardUrl;
    private String reverseUrl;
    private String methodName;
}
