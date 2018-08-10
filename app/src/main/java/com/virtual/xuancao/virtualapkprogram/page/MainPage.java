package com.virtual.xuancao.virtualapkprogram.page;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.virtual.xuancao.virtualapkprogram.BroadCast.BrocastConfig;
import com.virtual.xuancao.virtualapkprogram.PluginHelper.PluginConstant;
import com.virtual.xuancao.virtualapkprogram.PluginHelper.PluginHelper;
import com.virtual.xuancao.virtualapkprogram.R;
import com.virtual.xuancao.virtualapkprogram.db.DBEngine;
import com.virtual.xuancao.virtualapkprogram.db.bean.UserInfoDB;
import com.virtual.xuancao.virtualapkprogram.model.UserInfoModel;
import com.xuancao.base.BaseActivity;
import com.xuancao.base.Utils.PermissionUtils;


public class MainPage extends BaseActivity {

    private static final String TAG = "MainActivity";
    private TextView tv_login_status,tv_login_userInfo;
    private UserInfoModel userInfoModel;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        setContentView(R.layout.host_main_page);

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
        tv_login_status = (TextView) findViewById(R.id.tv_login_status);
        tv_login_userInfo = (TextView) findViewById(R.id.tv_login_userInfo);
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
        registerLoginReceiver();
        registerLoginOutReceiver();
        registerChangeInfoReceiver();
    }


    // attention:::UserInfoModel(宿主和插件中类名和包名必须相同)只是为了说明NativePlugin插件和宿主可以通过intent结合广播传递，
    // 实际项目开发中可以采用一套greendao共用缓存策略就行
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            String loginStatus = intent.getStringExtra("loginStatus");
            userInfoModel = (UserInfoModel) intent.getParcelableExtra("userInfo");
            switch (action){
                case BrocastConfig.LOGIN_SUCCESS_EVENT: //登录广播
                    tv_login_status.setText(loginStatus!=null ? loginStatus : "未登录");
                    tv_login_userInfo.setText(userInfoModel!=null ? userInfoModel.toString() : "用户信息");
                    if (userInfoModel!=null){
                        UserInfoDB userInfoDB = new UserInfoDB();
                        userInfoDB.setUser_id(userInfoModel.user_id+"");
                        userInfoDB.setAvatar(userInfoModel.avatar);
                        userInfoDB.setBirthday(userInfoModel.birthday);
                        userInfoDB.setGender(userInfoModel.gender);
                        userInfoDB.setMobile(userInfoModel.mobile);
                        userInfoDB.setNick_name(userInfoModel.nick_name);
                        DBEngine.getInstance().savePersonInfo(userInfoDB);
                    }
                    break;
                case BrocastConfig.LOGIN_OUT_EVENT:
                    tv_login_status.setText(loginStatus!=null ? loginStatus : "未登录");
                    tv_login_userInfo.setText(userInfoModel!=null ? userInfoModel.toString() : "用户信息");
                    DBEngine.getInstance().deletePersonInfo();
                    break;
                case BrocastConfig.CHANGE_USER_INFO:
                    UserInfoDB userInfoModel = DBEngine.getInstance().getPersonInfo();
                    tv_login_userInfo.setText(userInfoModel!=null ? userInfoModel.toString() : "用户信息");
                    break;
            }
        }
    };

    public void registerLoginReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(BrocastConfig.LOGIN_SUCCESS_EVENT);
        context.registerReceiver(broadcastReceiver, myIntentFilter);
    }

    public void registerLoginOutReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(BrocastConfig.LOGIN_OUT_EVENT);
        context.registerReceiver(broadcastReceiver, myIntentFilter);
    }

    public void registerChangeInfoReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(BrocastConfig.CHANGE_USER_INFO);
        context.registerReceiver(broadcastReceiver, myIntentFilter);
    }


    public void nativePlugin(){
        //定义一个布尔值 用来控制接收app和模块之间的跳转流程
        boolean isStart = PluginHelper.startActivity(this,
                PluginConstant.PLUGIN_ID_NATIVE,
                PluginConstant.PLUGIN_PACKAGE_NATIVE,
                "登录模块-NativePlugin",
                "com.native_plugin.xuancao.nativeplugin.pages.NativePage");
        if (!isStart) {
            Toast.makeText(this, "本地插件功能模块已损坏", Toast.LENGTH_SHORT).show();
        }
    }


    public void remotePlugin(){
        //定义一个布尔值 用来控制接收app和模块之间的跳转流程
        boolean isStart = PluginHelper.startActivity(this,
                PluginConstant.PLUGIN_ID_REMOTE,
                PluginConstant.PLUGIN_PACKAGE_REMOTE,
                "com.remote_plugin.xuancao.remoteplugin.page.RemoteHomePage");
        if (!isStart) {
            Toast.makeText(this, "服务器下载的插件功能模块已损坏", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }

}
