package com.lzh.demo.fragment.demo.demoretrofit.model;

import com.jason.common.network.HttpEngine;
import com.lzh.demo.fragment.demo.demoretrofit.entity.Subject;
import com.lzh.demo.fragment.demo.demoretrofit.http.DoubanHttpResultFunc;
import com.lzh.demo.fragment.demo.demoretrofit.http.DoubanMovieService;
import com.lzh.demo.fragment.mvp.BaseModel;
import com.lzh.demo.fragment.progress.SubscriberOnNextListener;

import java.util.List;

import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


/**
 * Created by liuzhenhui on 2016/11/8.
 */
public class GetMovieModel extends BaseModel {
    public static final String TAG = GetMovieModel.class.getSimpleName();
    public static final String BASE_URL = "https://api.douban.com/v2/movie/";

    private Retrofit retrofit;
    private DoubanMovieService movieService;

    public GetMovieModel() {
        retrofit = HttpEngine.getInstance().getRetrofit(BASE_URL);
    }

    public void getMovies(int start, int count, SubscriberOnNextListener listener) {
        Observable observable = getMovieService()
                .getTopMovie(start, count)
                .map(new DoubanHttpResultFunc<List<Subject>>());
        toSubscribeWithProgress(observable, listener);
    }

    public void getMovies(int start, int count, Subscriber subscriber) {
        Observable observable = getMovieService()
                .getTopMovie(start, count)
                .map(new DoubanHttpResultFunc<List<Subject>>());
        toSubscribe(observable, subscriber);
    }

    public void getMovies(int start, int count, Action1 action1) {
        Observable observable = getMovieService()
                .getTopMovie(start, count)
                .map(new DoubanHttpResultFunc<List<Subject>>());
        toSubscribe(observable, action1);
    }

    /**
     * 使用原生回掉
     */
    public Observable getMovies(int start, int count) {
        Observable observable = getMovieService()
                .getTopMovie(start, count)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new DoubanHttpResultFunc<List<Subject>>());
        return observable;
    }

    @Override
    public void destroy() {
        retrofit = null;
    }

    public DoubanMovieService getMovieService() {
        if (movieService == null) {
            movieService = retrofit.create(DoubanMovieService.class);
        }
        return movieService;
    }

}
