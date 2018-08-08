package com.virtual.xuancao.virtualapkprogram.eventBus.event;

/**
 * Created by xuancao on 2017/4/28.
 */

public class MsgEvent {

    public String type;
    public Object object;

    public MsgEvent(String type) {
        this.type = type;
    }

    public MsgEvent(Object object) {
        this.object = object;
    }

    public MsgEvent(String type, Object object) {
        this.type = type;
        this.object = object;
    }

    public Object getObject() {
        return object;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    /**
     * 注册成功消息
     */
    public static String REG_SUCCESS_EVENT = "reg_success_event";

    /**
     * 登录成功消息
     */
    public static String LOGIN_SUCCESS_EVENT = "login_success_event";



}
