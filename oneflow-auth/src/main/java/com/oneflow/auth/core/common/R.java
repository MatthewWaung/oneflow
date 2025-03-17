package com.oneflow.auth.core.common;

import com.oneflow.auth.security.core.constant.Constants;

import java.io.Serializable;

/**
 * 响应类主体
 */
public class R<T> implements Serializable {

    /**
     * 成功
     */
    public static final int SUCCESS = Constants.SUCCESS;
    public static final String SUCCESS_VALUE = Constants.OPERATION_SUCCESS;

    /**
     * 失败
     */
    public static final int FAIL = Constants.FAIL;
    private static final long serialVersionUID = 1L;

    private int code;

    private String msg;

    private T data;

    public static <T> R<T> success(){
        return restResult(null, SUCCESS, SUCCESS_VALUE);
    }

    public static <T> R<T> success(T data){
        return restResult(data, SUCCESS, SUCCESS_VALUE);
    }

    public static <T> R<T> success(T data, String msg){
        return restResult(data, SUCCESS, msg);
    }

    public static <T> R<T> fail(){
        return restResult(null, FAIL, null);
    }

    public static <T> R<T> fail(String msg){
        return restResult(null, FAIL, msg);
    }

    public static <T> R<T> fail(T data){
        return restResult(data, FAIL, null);
    }

    public static <T> R<T> fail(T data, String msg){
        return restResult(data, FAIL, msg);
    }

    public static <T> R<T> fail(int code, String msg){
        return restResult(null, code, msg);
    }

    public static <T> R<T> fail(T data, int code, String msg){
        return restResult(data, code, msg);
    }


    private static <T> R<T> restResult(T data, int code, String msg) {
        R<T> apiResult = new R<>();
        apiResult.setCode(code);
        apiResult.setData(data);
        apiResult.setMsg(msg);
        return apiResult;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
