package com.daehanins.mes.common.utils;

import com.daehanins.mes.common.vo.RestResponse;

public class RestUtil<T> {

    private RestResponse<T> response;

    public RestUtil(){
        response =new RestResponse<>();
        response.setSuccess(true);
        response.setCode(200);
        response.setMessage("success");
        response.setResult(null);
    }

    public RestResponse<T> setData(T t){
        this.response.setCode(200);
        this.response.setResult(t);
        return this.response;
    }

    public RestResponse<T> setData(T t, String message){
        this.setData(t);
        this.response.setMessage(message);
        return this.response;
    }

    public RestResponse<T> setMessage(String message){
        this.response.setMessage(message);
        return this.response;
    }

    public RestResponse<T> setError(String message){
        this.setError(500, message);
        return this.response;
    }

    public RestResponse<T> setError(Integer code, String message){
        this.response.setSuccess(false);
        this.response.setCode(code);
        this.response.setMessage(message);
        return this.response;
    }

    public RestResponse<T> setError(Integer code, String msg, T t) {
        this.response.setSuccess(false);
        this.response.setCode(code);
        this.response.setMessage(msg);
        this.response.setResult(t);
        return this.response;
    }

}
