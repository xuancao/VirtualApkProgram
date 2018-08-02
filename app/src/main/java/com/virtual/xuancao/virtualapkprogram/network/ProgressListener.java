package com.virtual.xuancao.virtualapkprogram.network;


public interface ProgressListener {
    void update(long bytesRead, long contentLength, boolean done);
}
