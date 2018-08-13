package com.remote_plugin.xuancao.remoteplugin.utils;

import android.net.Uri;
import android.os.Environment;

import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.TakePhotoOptions;

import java.io.File;


public class RemoteTakePhotoUtils {

    private static class LazyHolder {
        private static final RemoteTakePhotoUtils INSTANCE = new RemoteTakePhotoUtils();
    }

    private RemoteTakePhotoUtils() {
    }

    public static final RemoteTakePhotoUtils getInstance() {
        return RemoteTakePhotoUtils.LazyHolder.INSTANCE;
    }


    /**
     * 相册／拍照
     * @param takePhoto
     * @param type
     */
    public void onClick(TakePhoto takePhoto, int type) {
        File file = new File(RemoteFileUtils.getPhotoCacheDir() + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        Uri imageUri = Uri.fromFile(file);
        configCompress(takePhoto);
        configTakePhotoOption(takePhoto);
        switch (type) {
            case 0:
                takePhoto.onPickFromGalleryWithCrop(imageUri, getCropOptions());
                break;
            case 1:
                takePhoto.onPickFromCaptureWithCrop(imageUri, getCropOptions());
                break;
            default:
                break;
        }
    }

    private void configTakePhotoOption(TakePhoto takePhoto) {
        TakePhotoOptions.Builder builder = new TakePhotoOptions.Builder();
        builder.setWithOwnGallery(false);
        takePhoto.setTakePhotoOptions(builder.create());

    }

    private void configCompress(TakePhoto takePhoto) {
        takePhoto.onEnableCompress(null, false);
        int maxSize = 204800;
        int width = 800;
        int height = 800;
        CompressConfig config;
        config = new CompressConfig.Builder()
                .setMaxSize(maxSize)
                .setMaxPixel(width >= height ? width : height)
                .enableReserveRaw(true)
                .create();
        takePhoto.onEnableCompress(config, false);
    }

    private CropOptions getCropOptions() {
        int height = 400;
        int width = 400;
        CropOptions.Builder builder = new CropOptions.Builder();
        builder.setOutputX(width).setOutputY(height);
        builder.setWithOwnCrop(true);
        return builder.create();
    }
}
