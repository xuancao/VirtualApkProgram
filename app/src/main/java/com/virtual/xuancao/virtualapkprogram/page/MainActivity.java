package com.virtual.xuancao.virtualapkprogram.page;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.virtual.xuancao.virtualapkprogram.Helper.PluginHelper;
import com.virtual.xuancao.virtualapkprogram.PluginConstant;
import com.virtual.xuancao.virtualapkprogram.R;
import com.virtual.xuancao.virtualapkprogram.Utils.PermissionUtils;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (PermissionUtils.hasPermission()) {
            Log.d(TAG,"loadPlugin");
            PluginHelper.loadPlugin(this, PluginConstant.PLUGIN_ID_NATIVE);
            PluginHelper.loadPlugin(this,PluginConstant.PLUGIN_ID_REMOTE);
        } else {
            PermissionUtils.requestPermission(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (PermissionUtils.PERMISSION_REQUEST_CODE_STORAGE == requestCode) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                PermissionUtils.requestPermission(this);
            } else {
                PluginHelper.loadPlugin(this,PluginConstant.PLUGIN_ID_NATIVE);
                PluginHelper.loadPlugin(this,PluginConstant.PLUGIN_ID_REMOTE);
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    public void nativePlugin(View view){
        //定义一个布尔值 用来控制接收app和模块之间的跳转流程
        boolean isStart = PluginHelper.startActivity(this,
                PluginConstant.PLUGIN_ID_NATIVE,
                PluginConstant.PLUGIN_PACKAGE_NATIVE,
                "com.native_plugin.xuancao.nativeplugin.NativeActivity");
        if (!isStart) {
            Toast.makeText(this, "本地插件功能模块已损坏", Toast.LENGTH_SHORT).show();
        }
    }


    public void remotePlugin(View view){
        //定义一个布尔值 用来控制接收app和模块之间的跳转流程
        boolean isStart = PluginHelper.startActivity(this,
                PluginConstant.PLUGIN_ID_REMOTE,
                PluginConstant.PLUGIN_PACKAGE_REMOTE,
                "com.remote_plugin.xuancao.remoteplugin.RemoteActivity");
        if (!isStart) {
            Toast.makeText(this, "服务器下载的插件功能模块已损坏", Toast.LENGTH_SHORT).show();
        }
    }



}
