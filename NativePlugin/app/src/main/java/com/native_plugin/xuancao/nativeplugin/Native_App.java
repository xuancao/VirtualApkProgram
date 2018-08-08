package com.native_plugin.xuancao.nativeplugin;

import android.app.Application;
import android.content.Context;

public class Native_App extends Application{

    private static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }


    public static Context getInstance() {
        return mContext;
    }
}
