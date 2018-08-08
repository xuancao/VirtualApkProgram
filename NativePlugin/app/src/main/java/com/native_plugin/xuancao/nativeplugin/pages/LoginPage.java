package com.native_plugin.xuancao.nativeplugin.pages;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.gson.JsonObject;
import com.native_plugin.xuancao.nativeplugin.BroadCast.Native_BrocastConfig;
import com.native_plugin.xuancao.nativeplugin.Native_Func;
import com.native_plugin.xuancao.nativeplugin.R;
import com.native_plugin.xuancao.nativeplugin.eventBus.EventBusHelper;
import com.native_plugin.xuancao.nativeplugin.model.UserInfoModel;
import com.xuancao.base.BaseActivity;
import com.xuancao.base.Utils.LogUtil;
import com.xuancao.base.Utils.ToastUtil;
import com.xuancao.network.TypeModelHttpHandler;
import com.xuancao.network.XuancaoHttpClient;

public class LoginPage extends BaseActivity {

    public final String TAG = "Login";
    public EditText editText_phone;
    public EditText editText_password;
    public Button button_login;
    public CheckBox checkBox;
    private ImageView iv_back;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        setContentView(R.layout.page_login);

    }

    @Override
    public void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        editText_phone = (EditText) findViewById(R.id.editText_phone);
        editText_password = (EditText) findViewById(R.id.editText_password);
        button_login = (Button) findViewById(R.id.btn_login);
        checkBox = (CheckBox) findViewById(R.id.checkbox_pwd_state);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    editText_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    editText_password.setSelection(editText_password.getText().length());
                } else {
                    editText_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    editText_password.setSelection(editText_password.getText().length());
                }
            }
        });
    }

    @Override
    public void setListener() {
        button_login.setOnClickListener(this);
        iv_back.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClickEvent(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                Intent intent = new Intent(Native_BrocastConfig.LOGIN_SUCCESS_EVENT);
                intent.putExtra("loginStatus","已登录");
                sendBroadcast(intent);//发送标准广播
                EventBusHelper.loginSuccess(null);
                finish();
                break;
            case R.id.btn_login:
                if (TextUtils.isEmpty(editText_phone.getText().toString())) {
                    ToastUtil.show("请输入11位手机号");
                } else if (TextUtils.isEmpty(editText_password.getText().toString())) {
                    ToastUtil.show("请输入密码");
                } else if (!isMobileNO(editText_phone.getText().toString())) {
                    ToastUtil.show("请输入正确的手机号");
                } else if (editText_password.getText().toString().trim().length() < 6) {
                    ToastUtil.show("输入正确的密码");
                } else {
                    login(editText_phone.getText().toString(), editText_password.getText().toString());
                }
                break;
        }
    }

    public void login(String phone, String password) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("mobile", phone);
        jsonObject.addProperty("password", password);
        XuancaoHttpClient.post(Native_Func.URL_USER_LOGIN, jsonObject, new TypeModelHttpHandler<UserInfoModel>() {
            @Override
            public void onSuccess(UserInfoModel data) {
                ToastUtil.show("登录成功");
                LogUtil.i(TAG, data.toString());
//                UserHelper.setUserInfo(data.user_id, data.nick_name, data.avatar, data.api_token, data.birthday, data.gender, data.mobile);
                EventBusHelper.loginSuccess(data);

            }

            @Override
            public void onFailure(int errorCode, String errorMsg) {
                LogUtil.i(TAG, "errCode" + errorCode + "...errorMsg" + errorMsg);
                ToastUtil.show(errorMsg);
            }
        });
    }

    public static boolean isMobileNO(String mobiles) {
        if (!TextUtils.isEmpty(mobiles)) {
            if (mobiles.length() == 11 && mobiles.startsWith("1")) {
                return true;
            }
        }
        return false;
    }

}
