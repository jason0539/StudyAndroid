package com.jason.common.network.http;

/**
 * Created by liuzhenhui on 2016/11/17.
 */
public interface CommonSubscriberListener<T> {
    void onResult(T result, String msg);

    void onError(int code, String msg);
}
