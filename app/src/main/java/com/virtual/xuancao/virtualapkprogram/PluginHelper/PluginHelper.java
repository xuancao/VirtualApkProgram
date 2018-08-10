package com.virtual.xuancao.virtualapkprogram.PluginHelper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.didi.virtualapk.PluginManager;
import com.virtual.xuancao.virtualapkprogram.Utils.FileUtils;
import com.virtual.xuancao.virtualapkprogram.model.UserInfoModel;
import com.xuancao.base.Utils.LogUtil;

import java.io.File;

public class PluginHelper {
    private static String TAG = "xuancao";

    /**
     * 判断当前plugin是否已经加载
     * @param context，上下文
     * @param pluginId，插件id
     * @return
     */
    public static boolean isPluginLoaded(Context context, int pluginId){
        //初始化插件管理器
        PluginManager pluginManager = PluginManager.getInstance(context.getApplicationContext());
        //获取指定包名的插件对象
        if (pluginManager.getLoadedPlugin(getPluginPackageName(pluginId)) != null){
            return true;
        }
        return false;
    }

    /**
     * 根据插件id获取插件包名
     * @param pluginId
     * @return
     */
    public static String getPluginPackageName(int pluginId){
        String pluginPackage = "";
        switch (pluginId){
            //装车
            case PluginConstant.PLUGIN_ID_NATIVE:
                pluginPackage = PluginConstant.PLUGIN_PACKAGE_NATIVE;
                break;
            case PluginConstant.PLUGIN_ID_REMOTE:
                pluginPackage = PluginConstant.PLUGIN_PACKAGE_REMOTE;
                break;
        }
        return pluginPackage;
    }

    /**
     * 根据插件id获取插件名称
     * @param pluginId
     * @return
     */
    public static String getPluginName(int pluginId){
        String pluginName = "";
        switch (pluginId){
            //装车
            case PluginConstant.PLUGIN_ID_NATIVE:
                pluginName = PluginConstant.PLUGIN_NAME_NATIVE;
                break;
            case PluginConstant.PLUGIN_ID_REMOTE:
                pluginName = PluginConstant.PLUGIN_NAME_REMOTE;
                break;
        }
        return pluginName;
    }

    /**
     * 通过插件id获取插件文件对象
     * @param context
     * @param pluginId
     * @return, 返回插件文件对象
     */
    public static File getPluginFile(Context context, int pluginId){
        //定义插件apk的名称
        String pluginApkName = getPluginName(pluginId);
        //定义插件文件对象
        File file = new File(FileUtils.getPluginCacheDir(), pluginApkName);
        LogUtil.i(TAG, "getPluginFile:" +  file.getAbsolutePath());
        return file;
    }

    /**
     * 返回插件存储目录
     * @return
     */
    public static File getPluginDir(int pluginID){
        //插件存储目录
        return FileUtils.getPluginCacheDir();
    }

    /**
     * 根据插件id加载指定的插件
     * @param context，上下文对象
     * @param pluginId，插件id
     * @return, true:表示插件加载成功，false:表示插件加载失败
     */
    public static boolean loadPlugin(Context context, int pluginId){
        //先判断当前插件是否已经加载
        if (isPluginLoaded(context, pluginId)){
            return true;
        }
        //初始化插件管理器
        PluginManager pluginManager = PluginManager.getInstance(context.getApplicationContext());
        //获取插件APK的文件对象
        File apk = getPluginFile(context, pluginId);
        if (apk.exists()){
            try {
                //加载插件
                pluginManager.loadPlugin(apk);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else { //从网络上下载插件apk

        }
        return false;
    }

    /**
     * 定义一个方法，用来控制平台和模块之间的跳转
     * @param context 上下文环境
     * @param pluginId 模块名称
     * @param packageName 模块包名
     * @param className  模块页面名称
     */
    public static boolean startActivity(Context context, int pluginId, String packageName, String className){

        if(isPluginLoaded(context,pluginId)){
            //代表模块加载成功 页面可赢正常跳转功能

            try {
                Intent intent = new Intent();
                intent.setClassName(packageName, className);
                context.startActivity(intent);
            }catch (Exception e){
                e.printStackTrace();
                LogUtil.i(TAG, "startActivity" + "启动插件失败");
            }
            return true;
        }
        return false;
    }

    /**
     * 定义一个方法，用来控制平台和模块之间的跳转
     * @param context 上下文环境
     * @param pluginId 模块名称
     * @param packageName 模块包名
     * @param charSequence  传递参数
     * @param className  模块页面名称
     */
    public static boolean startActivity(Context context, int pluginId, String packageName, CharSequence charSequence, String className){

        if(isPluginLoaded(context,pluginId)){
            //代表模块加载成功 页面可赢正常跳转功能

            try {
                Intent intent = new Intent();
                intent.setClassName(packageName, className);
                intent.putExtra("name",charSequence);
                context.startActivity(intent);
            }catch (Exception e){
                e.printStackTrace();
                LogUtil.i(TAG, "startActivity" + "启动插件失败");
            }
            return true;
        }
        return false;
    }

    public static boolean startActivity(Context context, int pluginId, String packageName, UserInfoModel object, String className){

        if(isPluginLoaded(context,pluginId)){
            //代表模块加载成功 页面可赢正常跳转功能

            try {
                Intent intent = new Intent();
                intent.setClassName(packageName, className);
                Bundle bundle = new Bundle();
                bundle.putParcelable("userInfoBundle",object);
                intent.putExtra("userInfo",bundle);
                context.startActivity(intent);
            }catch (Exception e){
                e.printStackTrace();
                LogUtil.i(TAG, "startActivity" + "启动插件失败");
            }
            return true;
        }
        return false;
    }
}
