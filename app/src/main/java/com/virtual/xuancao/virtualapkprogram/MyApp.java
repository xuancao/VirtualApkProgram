package com.virtual.xuancao.virtualapkprogram;

import android.app.Application;
import android.content.Context;

import com.didi.virtualapk.PluginManager;
import com.virtual.xuancao.virtualapkprogram.Helper.DownPluginHelper;
import com.virtual.xuancao.virtualapkprogram.Utils.AssetsFileUtils;
import com.virtual.xuancao.virtualapkprogram.Utils.FileUtils;
import com.virtual.xuancao.virtualapkprogram.Utils.NetWorkUtil;
import com.xuancao.networklibrary.Func;

public class MyApp extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;

//        /** 从服务器下载插件到指定目录 */
//        if (NetWorkUtil.isWifi(this)) {
//            DownPluginHelper.downPlugin(Func.DOWN_LOAD_PLUGIN_URL);
//        }
        /**  将assets中插件拷贝到指定目录 */
        AssetsFileUtils.assertCopyPlugin("plugin", FileUtils.getPluginCacheDir().getPath());


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
