package com.virtual.xuancao.virtualapkprogram.model;

import java.io.Serializable;

public class UserInfoModel implements Serializable {
    
    private static final long serialVersionUID = 4848221984960393380L;

    public int user_id;

    public String nick_name;

    public String birthday;

    public String avatar;

    public String gender; //性别

    public String mobile; //手机号

    @Override
    public String toString() {
        return "UserInfoModel{" +
                "user_id=" + user_id +
                ", nick_name='" + nick_name + '\'' +
                ", birthday='" + birthday + '\'' +
                ", avatar='" + avatar + '\'' +
                ", gender='" + gender + '\'' +
                ", mobile='" + mobile + '\'' +
                '}';
    }
}
