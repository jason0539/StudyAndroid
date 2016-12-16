package com.jason.common.network.http;

import rx.Subscriber;

/**
 * Created by liuzhenhui on 2016/11/17.
 */
public class CommonSubscriber<T> extends Subscriber<HttpResult<T>> {

    CommonSubscriberListener commonSubscriberListener;

    public CommonSubscriber(CommonSubscriberListener listener) {
        commonSubscriberListener = listener;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        if (commonSubscriberListener != null) {
            commonSubscriberListener.onError(-1, "onError:" + e.toString());
        }
    }

    @Override
    public void onNext(HttpResult<T> httpResult) {
        if (httpResult.getStatus() == 0) {
            if (commonSubscriberListener != null) {
                commonSubscriberListener.onResult(httpResult.getRes(), httpResult.getMsg());
            }
        } else {
            if (commonSubscriberListener != null) {
                commonSubscriberListener.onError(httpResult.getStatus(), httpResult.getStatus() + ":" + httpResult.getMsg());
            }
        }
    }

}
