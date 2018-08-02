package com.virtual.xuancao.virtualapkprogram.network;

import android.os.Build;
import android.webkit.WebSettings;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.virtual.xuancao.virtualapkprogram.MyApp;
import com.virtual.xuancao.virtualapkprogram.ServerConfig;
import com.virtual.xuancao.virtualapkprogram.Utils.LogUtil;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by xuancao on 2017/7/04.
 */

public class XchuangHttpClient {

    public static String TAG = "XchuangHttpClient";
    private static OkHttpClient httpClient;
    private static final String USER_AGENT = "User-Agent";

    static {
        httpClient = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();
    }

    public static void download(final String url, Callback handler, final ProgressListener
            listener) {
        LogUtil.d(TAG, "download:------>" + url);
        OkHttpClient httpClient = new OkHttpClient.Builder().addNetworkInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response originalResponse = chain.proceed(chain.request());
                return originalResponse.newBuilder().body(new ProgressResponseBody(originalResponse.body(),
                        listener)).build();

            }
        }).build();
        Request request = new Request.Builder().url(url).removeHeader(USER_AGENT).addHeader(USER_AGENT, getUserAgent()).build();
        httpClient.newCall(request).enqueue(handler);
    }

    public static void get(final String func, final JsonObject jsonObject, final TypeBaseHttpHandler
            handler) {
        final String url = ServerConfig.getServerUrl(func);
        final String requestUrl = buildGetRequestParams(url, jsonObject);
        LogUtil.d(TAG, "RequestUrl:------>" + requestUrl);
        Request request = new Request.Builder()
//                .addHeader("api-token", UserHelper.getApiToken())
                .url(requestUrl).get().build();
        httpClient.newCall(request).enqueue(handler);
    }

    /**
     * author：xuancao
     * addHeader("Connection", "close")
     **/
    public static void post(final String func, final JsonObject jsonObject, final TypeBaseHttpHandler
            handler) {
        final String url = ServerConfig.getServerUrl(func);
        LogUtil.d(TAG, "RequestUrl:------>" + url);
        final FormBody requestParams = buildPostRequestParams(jsonObject);
        Request request = new Request.Builder().removeHeader(USER_AGENT)
                // .addHeader("Connection", "close")
                .addHeader(USER_AGENT, getUserAgent())
//                .addHeader("api-token", UserHelper.getApiToken())
                .post(requestParams).url(url).build();
        httpClient.newCall(request).enqueue(handler);
    }

    private static String buildGetRequestParams(String url, JsonObject jsonObject) {
        jsonObject = getCommonParams(jsonObject);
        jsonObject.addProperty("userId", "");
        jsonObject.addProperty("platform", "");
        jsonObject.addProperty("version", "");
        Set<Map.Entry<String, JsonElement>> entrySet = jsonObject.entrySet();
        Iterator<Map.Entry<String, JsonElement>> iterator = entrySet.iterator();
        StringBuilder builder = new StringBuilder(url);
        builder.append("?");
        while (iterator.hasNext()) {
            Map.Entry<String, JsonElement> next = iterator.next();
            if (next.getValue().isJsonNull()) {
                builder.append(next.getKey() + "=" + "&");
            } else {
                builder.append(next.getKey() + "=" + next.getValue().getAsString() + "&");
            }
        }
        return builder.delete(builder.length() - 1, builder.length()).toString();

    }

    private static FormBody buildPostRequestParams(JsonObject jsonObject) {
        jsonObject = getCommonParams(jsonObject);
        FormBody.Builder builder = new FormBody.Builder();
        Set<Map.Entry<String, JsonElement>> entrySet = jsonObject.entrySet();
        Iterator<Map.Entry<String, JsonElement>> iterator = entrySet.iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, JsonElement> next = iterator.next();
            if (next.getValue().isJsonNull()) {
                builder.add(next.getKey(), "");
            } else {
                String key = next.getKey();
                String value = next.getValue().getAsString();
                builder.add(key, value);
            }
        }
        LogUtil.d(TAG, "RequestParams:------>" + jsonObject.toString());
        return builder.build();
    }

    private static JsonObject getCommonParams(JsonObject jsonObject) {
        if (jsonObject == null) {
            jsonObject = new JsonObject();
        }
        if (!jsonObject.has("userId")) {
            jsonObject.addProperty("userId","");
        }
        jsonObject.addProperty("platform", "");
        jsonObject.addProperty("version", "");
        return jsonObject;
    }

    /**
     * 获取UserAgent(不允许存在中文，要将中文转码)
     * Header values are (technically) required to be ISO-8859-1 but in practice only ASCII really works
     * and OkHttp validates such (the exception has nothing to do with Retrofit). That string is a custom
     * user agent and you’ll need to restrict its contents to the ASCII character set when creating it.
     * see {@link Headers.Builder#checkNameAndValue(String, String)}
     */
    private static String getUserAgent() {
        String userAgent = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            userAgent = WebSettings.getDefaultUserAgent(MyApp.getmContext());
        } else {
            userAgent = System.getProperty("http.agent");
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0, length = userAgent.length(); i < length; i++) {
            char c = userAgent.charAt(i);
            if (c <= '\u001f' || c >= '\u007f') {
                sb.append(String.format("\\u%04x", (int) c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

}
