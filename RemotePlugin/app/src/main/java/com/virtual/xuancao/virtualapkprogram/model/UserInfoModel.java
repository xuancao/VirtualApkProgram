package com.virtual.xuancao.virtualapkprogram.model;


import android.os.Parcel;
import android.os.Parcelable;

public class UserInfoModel implements Parcelable {


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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.user_id);
        dest.writeString(this.nick_name);
        dest.writeString(this.birthday);
        dest.writeString(this.avatar);
        dest.writeString(this.gender);
        dest.writeString(this.mobile);
    }

    public UserInfoModel() {
    }

    protected UserInfoModel(Parcel in) {
        this.user_id = in.readInt();
        this.nick_name = in.readString();
        this.birthday = in.readString();
        this.avatar = in.readString();
        this.gender = in.readString();
        this.mobile = in.readString();
    }

    public static final Parcelable.Creator<UserInfoModel> CREATOR = new Parcelable.Creator<UserInfoModel>() {
        @Override
        public UserInfoModel createFromParcel(Parcel source) {
            return new UserInfoModel(source);
        }

        @Override
        public UserInfoModel[] newArray(int size) {
            return new UserInfoModel[size];
        }
    };
}
