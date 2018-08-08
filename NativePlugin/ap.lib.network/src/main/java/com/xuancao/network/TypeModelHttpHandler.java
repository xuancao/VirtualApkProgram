package com.xuancao.network;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.xuancao.base.Utils.LogUtil;
import com.xuancao.base.Utils.ToastUtil;
import com.xuancao.network.parser.Result;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by xuancao on 2017/7/04.
 * Desc:解析data节点为对象
 */

public abstract class TypeModelHttpHandler<T> extends TypeBaseHttpHandler {
    private boolean showToast = false;// 是否显示错误提示

    public TypeModelHttpHandler() {
    }

    public TypeModelHttpHandler(boolean showToast) {
        this.showToast = showToast;
    }

    public abstract void onSuccess(T data);

    public void onFirstLogin(T data){

    }


    @Override
    public void onBaseSuccess(String jsonString) {
        Result mResult = parseResponse(jsonString);
        T mData = null;
        if (mResult != null) {
            if (ServerCode.CODE_200 != mResult.getStatusCode()) {
                String errorInfo = mResult.getMessage();
                if (TextUtils.isEmpty(errorInfo)) {
                    errorInfo = "";
                }
                onError(mResult.getStatusCode(), errorInfo);
            }else {
                String resultStr = mResult.getData();
                if (!TextUtils.isEmpty(resultStr)) {
                    final Gson gson = new Gson();
                    try {
                        mData = gson.fromJson(resultStr, getGenericType());
                    } catch (Exception e) {
                        LogUtil.e(TAG, "json解析失败:" + e.getMessage());
                        onSuccess(mData);
                    }
                    onSuccess(mData);
                } else {
                    onSuccess(mData);
                }
            }
        }
    }

    protected void onError(int errorCode, String errorMsg) {
        if (showToast) {
            ToastUtil.show(errorMsg);
        }
        onFailure(errorCode, errorMsg);
    }

    public Type getGenericType() {
        Class<?> clazz = this.getClass();
        Type genericSuperclass = clazz.getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType) {
            Type[] typeArguments = ((ParameterizedType) genericSuperclass).getActualTypeArguments();
            Type type = typeArguments[0];
            return type;
        }
        return null;
    }

}
