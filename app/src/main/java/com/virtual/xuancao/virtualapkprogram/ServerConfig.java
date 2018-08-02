package com.virtual.xuancao.virtualapkprogram;

public class ServerConfig {


    /**
     * true 为debug状态，打印日志;false为上线发布状态
     */
    public static boolean IS_DEBUG = true;


    public static String SERVER_ROOT_PRODUCT = "http://zwj.app.api.cnwan.com";

    public static String getServerUrl(String func) {
        StringBuilder serverUrl = new StringBuilder(SERVER_ROOT_PRODUCT);
        serverUrl.append(func);
        return serverUrl.toString();
    }


}
