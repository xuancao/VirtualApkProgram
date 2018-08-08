package com.xuancao.network;

import android.os.Handler;
import android.os.Looper;

import com.xuancao.base.Utils.LogUtil;
import com.xuancao.network.parser.Result;
import com.xuancao.network.parser.ResultParser;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by xuancao on 2018/08/01.
 * Desc:初步解析 判断token
 */
public abstract class TypeBaseHttpHandler implements Callback {
    protected static final String TAG = XuancaoHttpClient.TAG;

    public TypeBaseHttpHandler() {
    }

    public abstract void onFailure(int errorCode, String errorMsg);

    public abstract void onBaseSuccess(String jsonString);

    protected abstract void onError(int code, String msg);

    @Override
    public void onResponse(Call call, Response response) {
        LogUtil.d(TAG, "ResponseCode------>" + response.code());
        final String jsonString;
        try {
            jsonString = response.body().string();
            LogUtil.d(TAG, "ResponseBody------>" + jsonString);
            //okhttp的回调为子线程 需要回调到主线程
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Result mResult = parseResponse(jsonString);
                    if (mResult != null) {
                        onBaseSuccess(jsonString);
                    } else {
                        String msg = "连接服务器失败，请检查网络是否正常";
                        onError(ServerCode.CODE_999, msg);
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            LogUtil.d(TAG, "ResponseException------>" + e.toString());
        }
    }

    @Override
    public void onFailure(Call call, IOException e) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                String msg = "连接服务器失败，请检查网络是否正常";
                onError(ServerCode.CODE_999, msg);
            }
        });
    }

    public static Object checkResponse(String jsonString) {
        Object result = null;
        if (jsonString != null) {
            jsonString = jsonString.trim();
            if (jsonString.startsWith("{")) {
                try {
                    result = new JSONTokener(jsonString).nextValue();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }


    public static Result parseResponse(String jsonString) {
        Object jsonResponse = checkResponse(jsonString);
        Result mResult = null;
        if (jsonResponse instanceof JSONObject) {
            JSONObject resultJson = (JSONObject) jsonResponse;
            try {
                mResult = ResultParser.parse(resultJson);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return mResult;
    }
}
