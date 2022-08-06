package com.pda.common.interceptor;

import cn.hutool.core.util.ObjectUtil;
import com.pda.api.dto.UserResDto;
import com.pda.common.redis.service.RedisService;
import com.pda.utils.SecurityUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Classname LoginInterceptor
 * @Description TODO
 * @Date 2022-08-06 16:44
 * @Created by AlanZhang
 */
@Component
public class UserInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisService redisService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String accessToken = request.getHeader("accessToken");
        if(StringUtils.isNotBlank(accessToken)){
            UserResDto userResDto = redisService.getCacheObject(accessToken);
            if(ObjectUtil.isNotNull(userResDto)){
                SecurityUtil.addCurrentUser(userResDto);
            }
        }
        return true;
    }

    /**
     * 避免内存泄露
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        SecurityUtil.remove();
    }
}
