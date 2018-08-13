package com.remote_plugin.xuancao.remoteplugin.page;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;
import com.remote_plugin.xuancao.remoteplugin.BroadCast.Remote_BrocastConfig;
import com.remote_plugin.xuancao.remoteplugin.R;
import com.remote_plugin.xuancao.remoteplugin.db.DBEngine;
import com.remote_plugin.xuancao.remoteplugin.db.bean.UserInfoDB;
import com.remote_plugin.xuancao.remoteplugin.utils.RemoteTakePhotoUtils;
import com.xuancao.base.BaseActivity;

import java.io.File;

public class RemoteUpdatePhotoPage extends BaseActivity implements TakePhoto.TakeResultListener, InvokeListener {

    private ImageView ivPhoto;
    private LinearLayout llChoosePhotoLayout,llTakePhotoLayout;
    private UserInfoDB userInfoModel;


    private TakePhoto mTakePhoto;
    private InvokeParam mInvokeParam;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        setContentView(R.layout.remote_page_modify_photo);
        userInfoModel = getIntent().getParcelableExtra("UserInfo");
    }

    @Override
    public void initView() {
        llChoosePhotoLayout = (LinearLayout) findViewById(R.id.llChoosePhotoLayout);
        llTakePhotoLayout = (LinearLayout) findViewById(R.id.llTakePhotoLayout);
        ivPhoto = (ImageView) findViewById(R.id.ivPhoto);
    }

    @Override
    public void setListener() {
        llChoosePhotoLayout.setOnClickListener(this);
        llTakePhotoLayout.setOnClickListener(this);
    }

    @Override
    public void onClickEvent(View v) {
        switch (v.getId()){
            case R.id.llChoosePhotoLayout:
                RemoteTakePhotoUtils.getInstance().onClick(getTakePhoto(), 0);
                break;
            case R.id.llTakePhotoLayout:
                RemoteTakePhotoUtils.getInstance().onClick(getTakePhoto(), 1);
                break;
        }
    }


    @Override
    public void initData() {
        if (userInfoModel!=null && (!TextUtils.isEmpty(userInfoModel.getAvatar()))){
            File mFile = new File(userInfoModel.getAvatar());
            if (mFile.exists()){
                ivPhoto.setImageURI(Uri.fromFile(mFile));
            }
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handlePermissionsResult(this, type, mInvokeParam, this);
    }

    public TakePhoto getTakePhoto() {
        if (mTakePhoto == null) {
            mTakePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
        }
        return mTakePhoto;
    }


    @Override
    public void takeSuccess(final TResult result) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
             @Override
             public void run() {
                 File mFile = new File(result.getImage().getCompressPath());
                 ivPhoto.setImageURI(Uri.fromFile(mFile));
                 if (userInfoModel!=null){
                     userInfoModel.setAvatar(result.getImage().getCompressPath());
                     DBEngine.getInstance().savePersonInfo(userInfoModel);

                     sendBroadcast(new Intent(Remote_BrocastConfig.CHANGE_USER_INFO));//发送标准广播
                 }
             }
        });
    }

    @Override
    public void takeFail(TResult result, String msg) {

    }

    @Override
    public void takeCancel() {
    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.mInvokeParam = invokeParam;
        }
        return type;
    }


}
