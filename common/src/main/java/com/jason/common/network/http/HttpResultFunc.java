package com.jason.common.network.http;


import com.jason.common.network.HttpException;
import com.jason.common.utils.MLog;

import rx.functions.Func1;

/**
 * Created by liuzhenhui on 2016/11/16.
 */
public class HttpResultFunc<T> implements Func1<HttpResult<T>, T> {
    public static final String TAG = HttpResultFunc.class.getSimpleName();

    @Override
    public T call(HttpResult<T> httpResult) {
        MLog.d(MLog.TAG_HTTP, TAG + "->" + "call " + httpResult.toString());
        if (httpResult.getStatus() != 0) {
            throw new HttpException(HttpException.NotFound);
        }
        return httpResult.getRes();
    }
}


