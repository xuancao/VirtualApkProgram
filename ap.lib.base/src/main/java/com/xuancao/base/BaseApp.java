package com.xuancao.base;

import android.app.Application;
import android.content.Context;

public class BaseApp {


    private Context mContext;
    /**
     * true 为debug状态，打印日志;false为上线发布状态
     */
    private boolean IS_DEBUG = false;


    private static Class<? extends BaseApp> sClazz = BaseApp.class;
    private volatile static BaseApp sInstance;

    public static BaseApp getInstance() {
        if (sInstance == null) {
            synchronized (BaseApp.class) {
                if (sInstance == null) {
                    try {
                        sInstance = sClazz.newInstance();
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        }
        return sInstance;
    }


    public void onCreate(Context context, Application app) {
        mContext = context;


    }


    public Context getContext() {
        return mContext;
    }

    public void setDEBUG(boolean debug){
        IS_DEBUG = debug;
    }

    public boolean getDeBug(){
        return IS_DEBUG;
    }






}
