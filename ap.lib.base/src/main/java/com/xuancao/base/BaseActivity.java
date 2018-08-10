package com.xuancao.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.xuancao.base.Utils.LogUtil;

/**
 * Desc:所有Activity的基类
 */
public abstract class BaseActivity extends Activity implements View.OnClickListener {
    protected String TAG = getClass().getSimpleName();
    protected Context context;

    private long clickTime = 0;
    private int tempViewId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;
        super.onCreate(savedInstanceState);
        onActivityCreated(savedInstanceState);
        initView();
        initData();
        setListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtil.i("BaseActivity", TAG + "->onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.i("BaseActivity", TAG + "->onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtil.i("BaseActivity", TAG + "->onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtil.i("BaseActivity", TAG + "->onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.i("BaseActivity", TAG + "->onDestroy");
    }

    /**
     * Activity跳转
     */
    public void startActivity(Class<?> clazz) {
        Intent intent = new Intent(context, clazz);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BaseApp.getInstance().getFinish_Act() && resultCode == RESULT_OK) {
            finishActivity();
        }
    }

    protected void finishActivity() {
        ((Activity) context).setResult(RESULT_OK);
        finish();
    }


    /**
     * @MethodName:onActivityCreated
     * @Description: 相当于onCreate, 为了不让子类显示super.onCreate()而诞生的方法
     */
    public abstract void onActivityCreated(Bundle savedInstanceState);

    public abstract void initView();

    /**
     * @MethodName:setListener
     * @Description: 设置监听 已在oncreate中调用
     */
    public abstract void setListener();

    /**
     * @param v void
     * @MethodName:onClickEvent
     * @Description: 点击事件包装
     */
    public abstract void onClickEvent(View v);
    /**
     * @MethodName:initData
     * @Description: 设置数据
     */
    public abstract void initData();

    @Override
    public void onClick(View v) {
        if ((System.currentTimeMillis() - clickTime) >  BaseApp.getInstance().getCLICK_INTERVAL()) {
            clickTime = System.currentTimeMillis();
        } else {
            if (tempViewId == v.getId()) {
                return;
            }
        }
        tempViewId = v.getId();
        onClickEvent(v);
    }


}
