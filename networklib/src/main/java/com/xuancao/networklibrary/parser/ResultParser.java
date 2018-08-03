package com.xuancao.networklibrary.parser;

import org.json.JSONException;
import org.json.JSONObject;


public class ResultParser {

    public static Result parse(JSONObject json) throws JSONException {
        Result mResult = new Result();

        if (json.has("code")) {
            mResult.setStatusCode(json.optInt("code"));
        }
        if (json.has("msg")) {
            mResult.setMessage(json.optString("msg"));
        }
        if (json.has("data")) {
            mResult.setData(json.optString("data"));
        }
        return mResult;
    }
}
