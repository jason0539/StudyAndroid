package com.jason.common.rxbus;


import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Created by liuzhenhui on 2016/11/9.
 */
public class RxBus {
    public static final String TAG = RxBus.class.getSimpleName();
    private final Subject<Object, Object> _bus = new SerializedSubject<>(PublishSubject.create());

    private RxBus() {

    }

    private static class LazyHolder {
        public static final RxBus INSTANCE = new RxBus();
    }

    public static final RxBus getInstance() {
        return LazyHolder.INSTANCE;
    }

    public void send(Object o) {
        _bus.onNext(o);
    }

    public Observable<Object> toObserverable() {
        return _bus;
    }
}
