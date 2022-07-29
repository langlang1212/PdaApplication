package com.pda.common;

import lombok.Data;

/**
 * @Classname Result
 * @Description TODO
 * @Date 2022-07-22 12:07
 * @Created by AlanZhang
 */
@Data
public class Result<T> {

    private String status;

    private String message;

    private T data;

    protected Result(String status,String message,T data){
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static <T> Result success(T data){
        return new Result(Constant.SUCCESS,Constant.SUCCESS_MSG,data);
    }

    public static Result success(){
        return success(Constant.SUCCESS_MSG);
    }

    /*public static Result success(String message){
        return success(message,null);
    }*/

    public static <T> Result success(String message,T data){
        return new Result(Constant.SUCCESS,message,data);
    }

    public static Result failed(){
        return success(Constant.FAILED_MSG);
    }

    public static Result failed(String message){
        return failed(message,null);
    }

    public static <T> Result failed(String message,T data){
        return new Result(Constant.FAILED,message,data);
    }
}
