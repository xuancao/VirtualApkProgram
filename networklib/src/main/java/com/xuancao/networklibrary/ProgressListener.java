package com.xuancao.networklibrary;


public interface ProgressListener {
    void update(long bytesRead, long contentLength, boolean done);
}
