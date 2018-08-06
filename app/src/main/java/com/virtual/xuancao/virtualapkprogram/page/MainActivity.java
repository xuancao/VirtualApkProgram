package com.virtual.xuancao.virtualapkprogram.page;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.virtual.xuancao.virtualapkprogram.PluginHelper.PluginConstant;
import com.virtual.xuancao.virtualapkprogram.PluginHelper.PluginHelper;
import com.virtual.xuancao.virtualapkprogram.R;
import com.xuancao.base.BaseActivity;
import com.xuancao.base.Utils.PermissionUtils;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
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


    @Override
    public void initView() {
        findViewById(R.id.btnNative).setOnClickListener(this);
        findViewById(R.id.btnRemote).setOnClickListener(this);
    }

    @Override
    public void setListener() {

    }

    @Override
    public void onClickEvent(View v) {
        switch (v.getId()){
            case R.id.btnRemote:
                remotePlugin();
                break;
            case R.id.btnNative:
                nativePlugin();
                break;
        }

    }

    @Override
    public void initData() {

    }

    public void nativePlugin(){
        //定义一个布尔值 用来控制接收app和模块之间的跳转流程
        boolean isStart = PluginHelper.startActivity(this,
                PluginConstant.PLUGIN_ID_NATIVE,
                PluginConstant.PLUGIN_PACKAGE_NATIVE,
                "参数ID",
                "com.native_plugin.xuancao.nativeplugin.NativeActivity");
        if (!isStart) {
            Toast.makeText(this, "本地插件功能模块已损坏", Toast.LENGTH_SHORT).show();
        }
    }


    public void remotePlugin(){
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
