package com.native_plugin.xuancao.nativeplugin.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserInfoModel implements Serializable {
    /**
     * "user_id": 5,
     * "api_token": "8C6tYrrcYx5zb2bHet7pDHMvrsnNrC279Pmjy0wipou1rYDi4sfo8isC7wpC",
     * "nick_name": "",
     * "birthday": null,
     * "avatar": ""
     */
    @SerializedName("user_id")
    public int user_id;

    @SerializedName("api_token")
    public String api_token;

    @SerializedName("nick_name")
    public String nick_name;

    @SerializedName("birthday")
    public String birthday;

    @SerializedName("avatar")
    public String avatar;

    @SerializedName("gender")
    public int gender; //性别

    @SerializedName("is_bind")
    public int is_bind; //是否绑定手机号 1绑定  2未绑定

    @SerializedName("mobile")
    public String mobile; //手机号

    @SerializedName("password_set")
    public boolean password_set; //是否设置密码

    @Override
    public String toString() {
        return "UserInfoModel{" +
                "user_id=" + user_id +
                ", api_token='" + api_token + '\'' +
                ", nick_name='" + nick_name + '\'' +
                ", birthday='" + birthday + '\'' +
                ", avatar='" + avatar + '\'' +
                ", gender=" + gender +
                ", is_bind=" + is_bind +
                ", mobile='" + mobile + '\'' +
                ", password_set=" + password_set +
                '}';
    }
}
