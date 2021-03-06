package com.virtual.xuancao.virtualapkprogram.eventBus;


import com.virtual.xuancao.virtualapkprogram.eventBus.event.MsgEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by xuancao on 2018/8/7.
 */

public class EventBusHelper {

    /**
     * 注册完成
     */
    public static void sendRegSuccess() {
        MsgEvent msgEvent = new MsgEvent(MsgEvent.REG_SUCCESS_EVENT);
        EventBus.getDefault().post(msgEvent);
    }

    /**
     * 登录成功
     */
    public static void loginSuccess(Object object) {
        MsgEvent msgEvent = new MsgEvent(MsgEvent.LOGIN_SUCCESS_EVENT,object);
        EventBus.getDefault().post(msgEvent);
    }

}
