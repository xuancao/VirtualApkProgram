package com.xuancao.network;

import android.text.TextUtils;

import com.xuancao.base.Utils.ToastUtil;
import com.xuancao.network.parser.Result;

/**
 * Desc:成功失败的返回结果
 */
public abstract class TypeResultHttpHandler extends TypeBaseHttpHandler {
    private boolean showToast = false;// 是否显示错误提示

    public TypeResultHttpHandler() {
    }

    public TypeResultHttpHandler(boolean showToast) {
        this.showToast = showToast;
    }

    public abstract void onSuccess(int statusCode, String msg);

    @Override
    public void onBaseSuccess(String jsonString) {
        Result mResult = parseResponse(jsonString);
        if (mResult != null) {
            if (mResult.getStatusCode() != ServerCode.CODE_200) {
                if (!TextUtils.isEmpty(mResult.getMessage())) {
                    onError(mResult.getStatusCode(), mResult.getMessage());
                }
            } else {
                onSuccess(mResult.getStatusCode(), mResult.getMessage());
            }
        }
    }

    protected void onError(int errorCode, String errorMsg) {
        if (showToast) {
            ToastUtil.show(errorMsg);
        }
        onFailure(errorCode, errorMsg);
    }

}
