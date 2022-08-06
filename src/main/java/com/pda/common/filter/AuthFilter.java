package com.pda.common.filter;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pda.common.config.IgnoreWhiteProperties;
import com.pda.common.redis.service.RedisService;
import com.pda.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Classname AuthFilter
 * @Description TODO
 * @Date 2022-08-06 17:02
 * @Created by AlanZhang
 */
//@Component
//@Slf4j
//public class AuthFilter extends OncePerRequestFilter {
//
//    @Autowired
//    private RedisService redisService;
//    @Autowired
//    private IgnoreWhiteProperties ignoreWhiteProperties;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        logger.info("鉴权拦截器!");
//        String servletPath = request.getServletPath();
//        if(!ignoreWhiteProperties.getWhites().contains(servletPath)){
//            String accessToken = request.getHeader("accessToken");
//            JSONObject jsonObject = new JSONObject();
//            if(StringUtils.isBlank(accessToken)){
//                jsonObject.put("status","401");
//                jsonObject.put("message","accessToken已过期");
//                response.setContentType("application/json");
//                response.setCharacterEncoding("UTF-8");
//                PrintWriter writer = response.getWriter();
//                writer.write(JSON.toJSONString(jsonObject));
//                return;
//            }else{
//                if(ObjectUtil.isNull(redisService.getCacheObject(accessToken))){
//                    jsonObject.put("status","401");
//                    jsonObject.put("message","登录用户失效!");
//                    response.setContentType("application/json");
//                    response.setCharacterEncoding("UTF-8");
//                    PrintWriter writer = response.getWriter();
//                    writer.write(JSON.toJSONString(jsonObject));
//                    return;
//                }
//            }
//        }
//        filterChain.doFilter(request,response);
//    }
//}
