package com.virtual.xuancao.virtualapkprogram.Utils;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.virtual.xuancao.virtualapkprogram.MyApp;
import com.xuancao.base.Utils.LogUtil;

import java.io.File;

public class FileUtils {

    private static Context context = MyApp.getmContext();
    private static String TAG = "xuancao";


    private static String CACHE_DIR_NAME_PLUGIN = "plugin";//插件文件目录
    private static String CACHE_DIR_NAME_OBJECTBOX = "objectbox";//数据库文件目录

//    /**
//     * 插件下载临时文件
//     */
//    public static File getDownloadApkFile(String pluginID,String version) {
//        File file = new File(getTempCacheDir(), "pluginId_" + pluginID +"_" + version +".apk");
//        return file;
//    }

    /**
     * 插件下载临时文件
     */
    public static File getDownloadApkFile(String pluginName) {
        File file = new File(getPluginCacheDir(), pluginName);
        return file;
    }

    /**
     * 获取/创建插件存储目录
     */
    public static File getPluginCacheDir() {
        String fileDirPath = getCacheRootPath() + CACHE_DIR_NAME_PLUGIN + File.separator;
        File fileDir = new File(fileDirPath);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        LogUtil.i(TAG, "TempCachePath=" + fileDir.getPath());
        return fileDir;
    }

    /**
     * 获取/创建插件存储目录
     */
    public static File getObjectBoxCacheDir() {
        String fileDirPath = getCacheRootPath() + CACHE_DIR_NAME_OBJECTBOX + File.separator;
        File fileDir = new File(fileDirPath);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        LogUtil.i(TAG, "TempCachePath=" + fileDir.getPath());
        return fileDir;
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
