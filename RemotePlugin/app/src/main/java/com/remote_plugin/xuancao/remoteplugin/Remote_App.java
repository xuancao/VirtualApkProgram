package com.remote_plugin.xuancao.remoteplugin;

import android.app.Application;

import com.xuancao.base.BaseApp;

public class Remote_App extends Application{


    private boolean IS_DEBUG = true;

    @Override
    public void onCreate() {
        super.onCreate();

        BaseApp.getInstance().onCreate(this,this);
        BaseApp.getInstance().setDEBUG(IS_DEBUG);

    }
}
