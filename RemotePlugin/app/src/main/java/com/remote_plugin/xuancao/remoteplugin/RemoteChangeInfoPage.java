package com.remote_plugin.xuancao.remoteplugin;

import android.os.Bundle;
import android.view.View;

import com.virtual.xuancao.virtualapkprogram.model.UserInfoModel;
import com.xuancao.base.BaseActivity;

public class RemoteChangeInfoPage extends BaseActivity {

    private UserInfoModel userInfoModel;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        setContentView(R.layout.remote_change_user_info);
        userInfoModel =(UserInfoModel) getIntent().getSerializableExtra("userInfo");
    }

    @Override
    public void initView() {

    }

    @Override
    public void setListener() {

    }

    @Override
    public void onClickEvent(View v) {

    }

    @Override
    public void initData() {

    }
}
