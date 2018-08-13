package com.remote_plugin.xuancao.remoteplugin.utils;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.remote_plugin.xuancao.remoteplugin.Remote_App;
import com.xuancao.base.Utils.LogUtil;

import java.io.File;

public class RemoteFileUtils {

    private static Context context = Remote_App.getInstance();
    private static String TAG = "xuancao";


    private static String CACHE_DIR_NAME_PHOTO = "photo";//插件文件目录

    /**
     * 获取/创建图像存储目录
     */
    public static String getPhotoCacheDir() {
        String fileDirPath = getCacheRootPath() + CACHE_DIR_NAME_PHOTO + File.separator;
        return fileDirPath;
    }

    public static String getCacheRootPath() {
        String path = getExternalCacheRootPath();
        // path = "/storage/emulated/0/Android/data/" + context.getPackageName() + "/cache/";
        if (TextUtils.isEmpty(path)) {
            path = getAppCacheRootPath();
            // path = "/data/data/" + context.getPackageName() + "/cache/";
        }
        LogUtil.i(TAG, "CacheRootPath=" + path);
        return path;
    }

    public static String getAppCacheRootPath() {
        return context.getCacheDir().getPath() + File.separator;
    }

    public static String getExternalCacheRootPath() {
        String path = "";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File fileDir = context.getExternalCacheDir();
            if (fileDir != null) {
                path = fileDir.getPath() + File.separator;
            }
        }
        return path;
    }
}
