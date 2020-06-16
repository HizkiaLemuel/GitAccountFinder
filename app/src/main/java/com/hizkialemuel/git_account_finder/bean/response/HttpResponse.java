package com.hizkialemuel.git_account_finder.bean.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public class HttpResponse<T> {
    private T meta;
    private int code;
    private String message;

    public T getMeta() {
        return meta;
    }

    public void setMeta(T meta) {
        this.meta = meta;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
