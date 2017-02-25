package com.lzh.demo.fragment.demo.demoretrofit.http;


import com.jason.common.network.HttpException;

import rx.functions.Func1;

/**
 * Created by liuzhenhui on 2016/11/9.
 *
 * 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
 *
 * @param <T> Subscriber真正需要的数据类型，也就是Data部分的数据类型
 */
public class DoubanHttpResultFunc<T> implements Func1<DoubanHttpResult<T>, T> {
    public static final String TAG = DoubanHttpResultFunc.class.getSimpleName();

    @Override
    public T call(DoubanHttpResult<T> httpResult) {
        if (httpResult.getCount() == 0) {
            throw new HttpException(HttpException.NotFound);
        }
        return httpResult.getSubjects();
    }
}

