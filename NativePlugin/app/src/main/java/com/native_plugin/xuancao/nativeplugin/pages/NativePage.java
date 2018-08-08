package com.native_plugin.xuancao.nativeplugin.pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.native_plugin.xuancao.nativeplugin.BroadCast.Native_BrocastConfig;
import com.native_plugin.xuancao.nativeplugin.R;
import com.native_plugin.xuancao.nativeplugin.eventBus.EventBusHelper;
import com.native_plugin.xuancao.nativeplugin.eventBus.event.MsgEvent;
import com.xuancao.base.BaseActivity;
import com.xuancao.base.Utils.LogUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


public class NativePage extends BaseActivity {

    private ImageView iv_back;
    private TextView tv_title,tv_page,tv_login_status;
    private CharSequence charSequence;
    private Button btn_go_register,btn_go_login,btn_go_login_out;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        setContentView(R.layout.page_native);
        charSequence =  getIntent().getCharSequenceExtra("name");
    }

    @Override
    public void initView() {
        EventBus.getDefault().register(this);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_page = (TextView)findViewById(R.id.native_tv_page);
        btn_go_register = (Button) findViewById(R.id.btn_go_register);
        btn_go_login = (Button) findViewById(R.id.btn_go_login);
        btn_go_login_out = (Button) findViewById(R.id.btn_go_login_out);
        tv_login_status = (TextView) findViewById(R.id.tv_login_status);
    }

    @Override
    public void setListener() {
        iv_back.setOnClickListener(this);
        btn_go_login.setOnClickListener(this);
        btn_go_register.setOnClickListener(this);
        btn_go_login_out.setOnClickListener(this);
    }

    @Override
    public void onClickEvent(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_go_register:
                startActivity(new Intent(this,RegisterPage.class));
                break;
            case R.id.btn_go_login:
                startActivity(new Intent(this,LoginPage.class));
                break;
            case R.id.btn_go_login_out:
                Intent intent = new Intent(Native_BrocastConfig.LOGIN_OUT_EVENT);
                sendBroadcast(intent);//发送标准广播
                EventBusHelper.loginOut();
                break;
        }

    }

    @Override
    public void initData() {

        if (charSequence !=null){
            tv_title.setText(charSequence);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(MsgEvent event) {
        LogUtil.e("xuancao------>",event.getType());
        if (event.getType().equals(MsgEvent.REG_SUCCESS_EVENT)){ //注册成功
            tv_login_status.setText("注册成功！");
        }else if (event.getType().equals(MsgEvent.LOGIN_SUCCESS_EVENT)) { //登录成功
            tv_login_status.setText("登录成功！");
        }else if (event.getType().equals(MsgEvent.LOGIN_OUT_EVENT)){
            tv_login_status.setText("未登录！");
        }
    }
}
