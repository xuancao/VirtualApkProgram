package com.remote_plugin.xuancao.remoteplugin.db;



import com.remote_plugin.xuancao.remoteplugin.db.bean.UserInfoDB;
import com.remote_plugin.xuancao.remoteplugin.db.dao.UserInfoDBDao;

import java.util.List;

/**
 *         数据转换
 */
public class DBEngine {

    private static DBEngine sEngine;

    public static DBEngine getInstance() {
        if (sEngine == null) {
            synchronized (DBEngine.class) {
                if (sEngine == null) {
                    try {
                        sEngine = new DBEngine();
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        }
        return sEngine;
    }

    public void initializeDB() {
        initPersonInfoDao();
    }

    /***
     * 操作个人信息数据库
     */
    private UserInfoDBDao mPersonInfoDao;

    public void initPersonInfoDao() {
        mPersonInfoDao = THDatabaseLoader.getInstance().mDaoSession.getUserInfoDBDao();
    }

    public void savePersonInfo(UserInfoDB personInfo) {
        if (personInfo == null) {
            return;
        }
        mPersonInfoDao.deleteAll();
        mPersonInfoDao.insert(personInfo);
    }


    public UserInfoDB getPersonInfo() {
        List<UserInfoDB> data = mPersonInfoDao.loadAll();
        if (data != null && data.size() > 0) {
            return data.get(0);
        }
        return null;
    }


    public void deletePersonInfo() {
        mPersonInfoDao.deleteAll();
    }



}
