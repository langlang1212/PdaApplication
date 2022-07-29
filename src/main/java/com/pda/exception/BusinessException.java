package com.pda.exception;

/**
 * @Classname BusinessException
 * @Description TODO
 * @Date 2022-07-22 12:03
 * @Created by AlanZhang
 */
public class BusinessException extends RuntimeException {

    private String code;

    private String message;

    /**
     * 空构造方法，避免反序列化问题
     */
    public BusinessException()
    {
    }

    public BusinessException(String message)
    {
        this.message = message;
    }

    public BusinessException(String code,String message){
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
