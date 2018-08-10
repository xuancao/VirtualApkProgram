package com.remote_plugin.xuancao.remoteplugin.db;

import android.content.Context;


import com.remote_plugin.xuancao.remoteplugin.db.dao.DaoMaster;
import com.remote_plugin.xuancao.remoteplugin.db.dao.DaoSession;

import org.greenrobot.greendao.database.Database;

/**
 * @author xiaolei.li
 */
public class THDatabaseLoader {

    private static final String DATABASE_NAME = "xuancao.db";

    public static Class<? extends THDatabaseLoader> implClazz = THDatabaseLoader.class;

    private volatile static THDatabaseLoader instance;

    public static THDatabaseLoader getInstance() {
        if (instance == null) {
            synchronized (THDatabaseLoader.class) {
                if (instance == null) {
                    try {
                        instance = implClazz.newInstance();
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        }
        return instance;
    }

    public THDevOpenHelper mHelper;
    public DaoSession mDaoSession;
    public Database mSQLiteDatabase;

    public void init(Context context) {
        mHelper = new THDevOpenHelper(context, DATABASE_NAME, null);
        mSQLiteDatabase = mHelper.getWritableDb();
        DaoMaster daoMaster = new DaoMaster(mSQLiteDatabase);
        mDaoSession = daoMaster.newSession();
    }
}
