package com.remote_plugin.xuancao.remoteplugin;

import android.app.Application;

import com.remote_plugin.xuancao.remoteplugin.db.DBEngine;
import com.remote_plugin.xuancao.remoteplugin.db.THDatabaseLoader;
import com.xuancao.base.BaseApp;

public class Remote_App extends Application{


    private boolean IS_DEBUG = true;

    @Override
    public void onCreate() {
        super.onCreate();

        BaseApp.getInstance().onCreate(this,this);
        BaseApp.getInstance().setDEBUG(IS_DEBUG);


        THDatabaseLoader.getInstance().init(this);
        DBEngine.getInstance().initializeDB();

    }
}
