package com.pda.exception;

import com.pda.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * @Classname GlobalExceptionHandler
 * @Description TODO
 * @Date 2022-07-22 12:05
 * @Created by AlanZhang
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    /**
     * 业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public Result handleServiceException(BusinessException e, HttpServletRequest request)
    {
        log.error(e.getMessage(), e);
        String code = e.getCode();
        return StringUtils.isNotBlank(code) ? Result.failed(code, e.getMessage()) : Result.failed(e.getMessage());
    }
}
