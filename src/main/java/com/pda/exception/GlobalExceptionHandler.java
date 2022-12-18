package com.pda.exception;

import com.pda.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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

    //捕获校验失败的异常
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    //设置响应状态码，当然这里其实我并没有用上，我自己在R_对象封装了
    @ResponseStatus(HttpStatus.OK)
    public Result handleParamCheckExcepion(HttpServletRequest req, MethodArgumentNotValidException ex) {
        //那下面这里就是读取具体的校验失败的数据了，可能不是一个，所以其实有一个遍历的过程。
        BindingResult bindingResult = ex.getBindingResult();
        StringBuilder paramErrorMsg = new StringBuilder();
        if (bindingResult.hasErrors()) {

            List<ObjectError> allErrors = bindingResult.getAllErrors();
            for (ObjectError objectError : allErrors) {
                String msg = objectError.getDefaultMessage();
                if (paramErrorMsg.length() == 0) {
                    paramErrorMsg.append(msg);
                } else {
                    paramErrorMsg.append(',');
                    paramErrorMsg.append(msg);
                }
            }
        }
        //这里我们输出了错误的日志
        log.error("参数校验失败! uri:{},错误信息:{}", req.getRequestURI(), paramErrorMsg.toString());
        //这里我用R_对象去封装返回了
        return Result.failed(paramErrorMsg.toString());
    }
}
