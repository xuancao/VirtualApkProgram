package com.virtual.xuancao.virtualapkprogram;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.virtual.xuancao.virtualapkprogram.Utils.AssetsFileUtils;
import com.virtual.xuancao.virtualapkprogram.Utils.FileUtils;
import com.virtual.xuancao.virtualapkprogram.Utils.LogUtil;
import com.virtual.xuancao.virtualapkprogram.Utils.NetWorkUtil;
import com.virtual.xuancao.virtualapkprogram.Utils.PermissionUtils;
import com.virtual.xuancao.virtualapkprogram.network.ProgressListener;
import com.virtual.xuancao.virtualapkprogram.network.XchuangHttpClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * 将assets中插件拷贝到指定目录
         */
        AssetsFileUtils.assertCopyPlugin("plugin", FileUtils.getPluginCacheDir().getPath());

        if (PermissionUtils.hasPermission()) {
            Log.d(TAG,"loadPlugin");
            PluginHelper.loadPlugin(this,PluginConstant.PLUGIN_ID_NATIVE);
            PluginHelper.loadPlugin(this,PluginConstant.PLUGIN_ID_REMOTE);
        } else {
            PermissionUtils.requestPermission(this);
        }



//        if (NetWorkUtil.isWifi(this)) {
//            downPlugin("ddddddddddddddddddddddddddd");
//        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (PermissionUtils.PERMISSION_REQUEST_CODE_STORAGE == requestCode) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                PermissionUtils.requestPermission(this);
            } else {
                PluginHelper.loadPlugin(this,PluginConstant.PLUGIN_ID_NATIVE);
                PluginHelper.loadPlugin(this,PluginConstant.PLUGIN_ID_REMOTE);
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    public void nativePlugin(View view){
        //定义一个布尔值 用来控制接收app和模块之间的跳转流程
        boolean isStart = PluginHelper.startActivity(this,
                PluginConstant.PLUGIN_ID_NATIVE,
                PluginConstant.PLUGIN_PACKAGE_NATIVE,
                "com.native_plugin.xuancao.nativeplugin.NativeActivity");
        if (!isStart) {
            Toast.makeText(this, "本地插件功能模块已损坏", Toast.LENGTH_SHORT).show();
        }
    }


    public void remotePlugin(View view){
        //定义一个布尔值 用来控制接收app和模块之间的跳转流程
        boolean isStart = PluginHelper.startActivity(this,
                PluginConstant.PLUGIN_ID_REMOTE,
                PluginConstant.PLUGIN_PACKAGE_REMOTE,
                "com.remote_plugin.xuancao.remoteplugin.RemoteActivity");
        if (!isStart) {
            Toast.makeText(this, "服务器下载的插件功能模块已损坏", Toast.LENGTH_SHORT).show();
        }
    }


    private boolean isInterceptDownload = false;
    private int progress = 0;

    public void downPlugin(final String url){
        new Thread(new Runnable() {
            @Override
            public void run() {
                XchuangHttpClient.download(url, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {

                        long length = response.body().contentLength();
                        InputStream is = response.body().byteStream();
                        File file = FileUtils.getPluginCacheDir();
                        String apkName = file.getAbsolutePath() + PluginConstant.PLUGIN_NAME_REMOTE;
                        File apkFile = new File(apkName);
                        FileOutputStream fos = new FileOutputStream(apkFile);
                        int count = 0;
                        byte buf[] = new byte[1024];
                        do {
                            int numRead = is.read(buf);
                            count += numRead;
                            progress = (int) (((float) count / length) * 100);

                            fos.write(buf, 0, numRead);
                            // 当点击取消时，则停止下载
                        } while (!isInterceptDownload);
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
