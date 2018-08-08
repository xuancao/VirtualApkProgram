package com.native_plugin.xuancao.nativeplugin.pages;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import com.native_plugin.xuancao.nativeplugin.Native_Func;
import com.native_plugin.xuancao.nativeplugin.R;
import com.native_plugin.xuancao.nativeplugin.eventBus.EventBusHelper;
import com.virtual.xuancao.virtualapkprogram.model.UserInfoModel;
import com.native_plugin.xuancao.nativeplugin.utils.AppUtil;
import com.xuancao.base.BaseActivity;
import com.xuancao.base.Utils.LogUtil;
import com.xuancao.base.Utils.ToastUtil;
import com.xuancao.network.TypeModelHttpHandler;
import com.xuancao.network.TypeResultHttpHandler;
import com.xuancao.network.XuancaoHttpClient;

public class RegisterPage extends BaseActivity {


    private ImageView iv_back;
    public EditText edit_phone;
    public EditText edit_code;
    public EditText edit_password;
    public Button btn_get_code;
    public Button btn_register;
    public CheckBox checkbox_pwd_state;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        setContentView(R.layout.native_page_register);
    }

    @Override
    public void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        edit_phone = (EditText) findViewById(R.id.editText_phone);
        edit_code = (EditText) findViewById(R.id.editText_code);
        edit_password = (EditText) findViewById(R.id.editText_password);
        btn_get_code = (Button) findViewById(R.id.btn_get_code);
        btn_register = (Button) findViewById(R.id.btn_quick_register);
        checkbox_pwd_state = (CheckBox) findViewById(R.id.checkbox_pwd_state);
        checkbox_pwd_state.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    edit_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    edit_password.setSelection(edit_password.getText().length());
                } else {
                    edit_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    edit_password.setSelection(edit_password.getText().length());
                }
            }
        });
    }

    @Override
    public void setListener() {
        iv_back.setOnClickListener(this);
        btn_get_code.setOnClickListener(this);
        btn_register.setOnClickListener(this);
    }

    @Override
    public void onClickEvent(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_quick_register:
                if (TextUtils.isEmpty(edit_phone.getText().toString())) {
                    ToastUtil.show("请输入11位手机号");
                } else if (TextUtils.isEmpty(edit_code.getText().toString())) {
                    ToastUtil.show("验证码不能为空");
                } else if (TextUtils.isEmpty(edit_password.getText().toString())) {
                    ToastUtil.show("请输入密码");
                } else if (!isMobileNO(edit_phone.getText().toString())) {
                    ToastUtil.show("请输入正确的手机号");
                } else if (edit_code.getText().toString().length() < 4) {
                    ToastUtil.show("输入四位验证码");
                } else if (edit_password.getText().toString().length() < 6) {
                    ToastUtil.show("密码不能少于6位");
                } else {
                    register(edit_phone.getText().toString(), edit_password.getText().toString(), edit_code.getText().toString());
                }

                break;
            case R.id.btn_get_code:
                if (TextUtils.isEmpty(edit_phone.getText().toString()) || (!isMobileNO(edit_phone.getText().toString()))) {
                    ToastUtil.show("请输入正确的手机号");
                } else {
                    getCode(edit_phone.getText().toString());
                }
                break;
        }
    }

    @Override
    public void initData() {

    }

    private CountDownTimer countDownTimer = new CountDownTimer(60000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            btn_get_code.setText(millisUntilFinished / 1000 + "s后重发");
        }

        @Override
        public void onFinish() {
            btn_get_code.setText("发送验证码");
            btn_get_code.setEnabled(true);
        }
    };

    public void getCode(String phone) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("mobile", phone);
        XuancaoHttpClient.post(Native_Func.URL_USER_GETCODE, jsonObject, new TypeResultHttpHandler() {
            @Override
            public void onFailure(int errorCode, String errorMsg) {
                LogUtil.e("----------->",errorMsg);
            }

            @Override
            public void onSuccess(int statusCode, String msg) {
                countDownTimer.start();
                btn_get_code.setEnabled(false);
                LogUtil.e("----------->",msg);
            }
        });

    }

    public void register(String phone, String password, String code) {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("mobile", phone);
        jsonObject.addProperty("password", password);
        jsonObject.addProperty("code", code);
        jsonObject.addProperty("device_id", AppUtil.getUniquePsuedoID());
        jsonObject.addProperty("mac", AppUtil.getMacAddress(context));
        jsonObject.addProperty("name", AppUtil.getPhoneBrand() + "/" + AppUtil.getPhoneModel());
        jsonObject.addProperty("platform", "android");
        XuancaoHttpClient.post(Native_Func.URL_USER_REGISTER, jsonObject, new TypeModelHttpHandler<UserInfoModel>() {
            @Override
            public void onSuccess(UserInfoModel data) {
                ToastUtil.show("注册成功");
                EventBusHelper.sendRegSuccess();
            }

            @Override
            public void onFailure(int errorCode, String errorMsg) {
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
