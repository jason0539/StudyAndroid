package com.lzh.demo.fragment.mvp;


import com.jason.common.network.http.CommonSubscriber;
import com.jason.common.network.http.CommonSubscriberListener;
import com.lzh.demo.fragment.progress.ProgressSubscriber;
import com.lzh.demo.fragment.progress.SubscriberOnNextListener;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by liuzhenhui on 2016/11/4.
 * Model基类，提供基础接口，方便Presenter销毁时统一销毁处理
 */
public abstract class BaseModel {
    public static final String TAG = BaseModel.class.getSimpleName();


    protected <T> void toSubscribe(Observable<T> o, Subscriber<T> s) {
        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }

    protected <T> void toSubscribe(Observable<T> o, Action1<T> action1) {
        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1);
    }

    protected <T> void toSubscribeWithProgress(Observable<T> o, SubscriberOnNextListener listener) {
        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressSubscriber(listener));
    }


    protected <T> void toSubscribe(Observable<T> o, CommonSubscriberListener<T> commonSubscriberListener) {
        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonSubscriber(commonSubscriberListener));
    }

    public abstract void destroy();
}
