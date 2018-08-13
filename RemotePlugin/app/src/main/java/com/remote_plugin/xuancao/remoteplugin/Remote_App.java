package com.remote_plugin.xuancao.remoteplugin;

import android.app.Application;
import android.content.Context;

import com.remote_plugin.xuancao.remoteplugin.db.DBEngine;
import com.remote_plugin.xuancao.remoteplugin.db.THDatabaseLoader;
import com.xuancao.base.BaseApp;

public class Remote_App extends Application{


    private boolean IS_DEBUG = true;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        BaseApp.getInstance().onCreate(this,this);
        BaseApp.getInstance().setDEBUG(IS_DEBUG);


        THDatabaseLoader.getInstance().init(this);
        DBEngine.getInstance().initializeDB();

    }

    public static Context getInstance(){
        return context;
    }
}
