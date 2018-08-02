package com.virtual.xuancao.virtualapkprogram;

import android.app.Application;
import android.content.Context;

import com.didi.virtualapk.PluginManager;

public class MyApp extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        PluginManager.getInstance(base).init();
    }

    public static Context getmContext() {
        return mContext;
    }
}
