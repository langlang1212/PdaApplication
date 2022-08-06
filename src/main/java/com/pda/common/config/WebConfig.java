package com.pda.common.config;

import com.pda.common.interceptor.UserInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Classname WebConfig
 * @Description TODO
 * @Date 2022-08-06 17:12
 * @Created by AlanZhang
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private UserInterceptor userInterceptor;

    //注册拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(userInterceptor).addPathPatterns("/**").excludePathPatterns("/pda/login").excludePathPatterns("/doc.html");
    }
}
