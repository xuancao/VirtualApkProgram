package com.virtual.xuancao.virtualapkprogram.Utils;

import com.virtual.xuancao.virtualapkprogram.MyApp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class AssetsFileUtils {

    public static void assertCopyPlugin(String pluginDirPath,String targetDirFullPath){
        try {
            String[] listFiles = MyApp.getmContext().getAssets().list(pluginDirPath);
            for (String string : listFiles) {
                File file = new File(targetDirFullPath + File.separator + string);
                if (string.endsWith(".apk") && (!file.exists())) {    // 从assets目录下拷贝文件
                    InputStream assestsFileImputStream;
                    try {
                        assestsFileImputStream = MyApp.getmContext().getAssets().open(pluginDirPath + File.separator + string);
                        copyFile(assestsFileImputStream, targetDirFullPath + File.separator + string);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void copyFile(InputStream in, String targetPath) {
        try {
            FileOutputStream fos = new FileOutputStream(new File(targetPath));
            byte[] buffer = new byte[1024];
            int byteCount = 0;
            while ((byteCount = in.read(buffer)) != -1) {// 循环从输入流读取
                // buffer字节
                fos.write(buffer, 0, byteCount);// 将读取的输入流写入到输出流
            }
            fos.flush();// 刷新缓冲区
            in.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
