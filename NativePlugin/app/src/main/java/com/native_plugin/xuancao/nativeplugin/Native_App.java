package com.native_plugin.xuancao.nativeplugin;

import android.app.Application;
import android.content.Context;

import com.xuancao.base.BaseApp;

public class Native_App extends Application{

    private static Context mContext;
    private boolean IS_DEBUG = true;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;

        BaseApp.getInstance().onCreate(this,this);
        BaseApp.getInstance().setDEBUG(IS_DEBUG);

    }


    public static Context getInstance() {
        return mContext;
    }
}
