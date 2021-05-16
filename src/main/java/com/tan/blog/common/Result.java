package com.tan.blog.common;

import java.util.List;

public class Result<T> {

    private Boolean success;

    private String message;

    private List<T> list;

    public Result(Boolean success,String message){
        this.success = success;
        this.message = message;
    }
    public Result(Boolean success, String message, List<T> list){
        this.success = success;
        this.message = message;
        this.list = list;
    }

    public static Result ok(String message){
        return new Result(true,message);
    }

    public static Result fail(String message){
        return new Result(false,message);
    }


    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
