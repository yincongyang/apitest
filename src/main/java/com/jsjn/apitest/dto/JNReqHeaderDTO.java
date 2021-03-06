package com.jsjn.apitest.dto;

import java.io.Serializable;

/**
 * 金农网关响应系统级参数
 * <p>
 * Created by yincongyang on 17/10/16.
 */
public class JNReqHeaderDTO<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private String api_key;

    private String call_dt;

    private String format;

    private String ie;

    private String signature;

    private T req_body;

    public String getApi_key() {
        return api_key;
    }

    public void setApi_key(String api_key) {
        this.api_key = api_key;
    }

    public String getCall_dt() {
        return call_dt;
    }

    public void setCall_dt(String call_dt) {
        this.call_dt = call_dt;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getIe() {
        return ie;
    }

    public void setIe(String ie) {
        this.ie = ie;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public T getReq_body() {
        return req_body;
    }

    public void setReq_body(T req_body) {
        this.req_body = req_body;
    }
}
