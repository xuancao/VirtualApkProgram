package com.xuancao.network;


public interface ProgressListener {
    void update(long bytesRead, long contentLength, boolean done);
}
