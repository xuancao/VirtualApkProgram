package com.virtual.xuancao.virtualapkprogram.network.parser;

public class Result {
    private int code; // 响应码
    private String msg; // 消息
    private String data;// 具体的业务有不同的返回结果

    public int getStatusCode() {
        return code;
    }

    public void setStatusCode(int statusCode) {
        this.code = statusCode;
    }

    public String getMessage() {
        return msg;
    }

    public void setMessage(String message) {
        this.msg = message;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
