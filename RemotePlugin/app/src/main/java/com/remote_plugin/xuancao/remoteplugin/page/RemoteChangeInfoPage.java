package com.remote_plugin.xuancao.remoteplugin.page;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.remote_plugin.xuancao.remoteplugin.BroadCast.Remote_BrocastConfig;
import com.remote_plugin.xuancao.remoteplugin.R;
import com.remote_plugin.xuancao.remoteplugin.db.DBEngine;
import com.remote_plugin.xuancao.remoteplugin.db.bean.UserInfoDB;
import com.xuancao.base.BaseActivity;

public class RemoteChangeInfoPage extends BaseActivity {

    private ImageView iv_back;
    private TextView remote_tv_page,tv_login_status;
    private Button btn_change_userInfo;

    private UserInfoDB userInfoModel;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        setContentView(R.layout.remote_change_user_info);
        userInfoModel = getIntent().getParcelableExtra("UserInfo");
    }

    @Override
    public void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        remote_tv_page = (TextView) findViewById(R.id.remote_tv_page);
        tv_login_status = (TextView) findViewById(R.id.tv_login_status);
        btn_change_userInfo = (Button) findViewById(R.id.btn_change_userInfo);
    }

    @Override
    public void setListener() {
        iv_back.setOnClickListener(this);
        btn_change_userInfo.setOnClickListener(this);
    }

    @Override
    public void onClickEvent(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_change_userInfo:
                if (userInfoModel != null){
                    changeUserInfo();
                }
                break;
        }
    }

    @Override
    public void initData() {
        tv_login_status.setText(userInfoModel!=null ? "已登录"+userInfoModel.getNick_name() : "未登录");
        remote_tv_page.setText(userInfoModel!=null ? userInfoModel.toString():"用户信息");

    }

    private void changeUserInfo(){
        userInfoModel.setGender("女");
        userInfoModel.setBirthday("19910808");
        userInfoModel.setMobile("139。。");
        userInfoModel.setNick_name("caoxuan");
        DBEngine.getInstance().savePersonInfo(userInfoModel);
        sendBroadcast(new Intent(Remote_BrocastConfig.CHANGE_USER_INFO));//发送标准广播

        remote_tv_page.setText(userInfoModel!=null ? userInfoModel.toString():"用户信息");
    }



}
