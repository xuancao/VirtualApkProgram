package com.virtual.xuancao.virtualapkprogram.Helper;

import com.virtual.xuancao.virtualapkprogram.PluginConstant;
import com.virtual.xuancao.virtualapkprogram.Utils.FileUtils;
import com.xuancao.base.Utils.LogUtil;
import com.xuancao.base.Utils.ToastUtil;
import com.xuancao.network.ProgressListener;
import com.xuancao.network.XuancaoHttpClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class DownPluginHelper {

    private static boolean isInterceptDownload = false;

    public static void downPlugin(final String url){
        new Thread(new Runnable() {
            @Override
            public void run() {
                XuancaoHttpClient.download(url, new Callback() {

                    @Override
                    public void onFailure(Call call, IOException e) {
                        ToastUtil.show(e.getMessage());
                    }

                    @Override
                    public void onResponse(Call call, Response response) {
                        try {
                            InputStream is = response.body().byteStream();
                            File file = FileUtils.getPluginCacheDir();
                            LogUtil.e("dd",file.getAbsolutePath());
                            String apkName = file.getAbsolutePath() + File.separator + PluginConstant.PLUGIN_NAME_REMOTE;
                            File apkFile = new File(apkName);
                            FileOutputStream fos = new FileOutputStream(apkFile);
                            byte buf[] = new byte[1024];
                            do {
                                int numRead = is.read(buf);
                                fos.write(buf, 0, numRead);
                                // 当点击取消时，则停止下载
                            } while (!isInterceptDownload);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }, new ProgressListener() {
                    @Override
                    public void update(long bytesRead, long contentLength, boolean done) {
                        LogUtil.i("xuancao","bytesRead"+bytesRead+".....contentLength"+contentLength+"...done"+done);
                    }
                });
            }
        }).start();
   }

}
