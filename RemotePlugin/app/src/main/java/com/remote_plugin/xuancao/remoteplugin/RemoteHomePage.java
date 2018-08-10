package com.remote_plugin.xuancao.remoteplugin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.virtual.xuancao.virtualapkprogram.model.UserInfoModel;
import com.xuancao.base.BaseActivity;

public class RemoteHomePage extends BaseActivity {

    private ImageView iv_back;
    private TextView remote_user_info;
    private Button btn_go_change_userInfo;

    private UserInfoModel userInfoModel;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        setContentView(R.layout.remote_home_page);
//        userInfoModel = getIntent().getBundleExtra("userInfo").getParcelable("userInfoBundle");
    }

    @Override
    public void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        remote_user_info = (TextView) findViewById(R.id.remote_user_info);
        btn_go_change_userInfo = (Button) findViewById(R.id.btn_go_change_userInfo);
    }

    @Override
    public void setListener() {
        iv_back.setOnClickListener(this);
        btn_go_change_userInfo.setOnClickListener(this);
    }

    @Override
    public void onClickEvent(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_go_change_userInfo:
                Intent intent = new Intent(this,RemoteChangeInfoPage.class);
                intent.putExtra("userInfo",userInfoModel);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void initData() {
        remote_user_info.setText(userInfoModel.toString());
    }

}
