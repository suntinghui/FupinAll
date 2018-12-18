package com.jkrm.fupin.bean;

import java.io.Serializable;

public class ResponseBean<T> implements Serializable {
    private static final int CODE_SUCCESS = 0;
    private static final int CODE_TOKEN_ERROR = 110;

    private int code;
    private String msg;
    private T data;

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

    public boolean isSuccess() {
        return code == CODE_SUCCESS;
    }

    public boolean isTokenError() {
        return code == CODE_TOKEN_ERROR;
    }
}
