package com.xuancao.network;

public class ServerConfig {


    public static String SERVER_ROOT_PRODUCT = "http://zwdj.app.api.cnwan.com";

    public static String getServerUrl(String func) {
        StringBuilder serverUrl = new StringBuilder(SERVER_ROOT_PRODUCT);
        serverUrl.append(func);
        return serverUrl.toString();
    }


}
