package com.jason.workdemo.page.demo.demoretrofit.presenter;


import com.jason.workdemo.page.demo.demoretrofit.RxRetrofitContract;
import com.jason.workdemo.page.demo.demoretrofit.entity.Subject;
import com.jason.workdemo.page.demo.demoretrofit.model.GetMovieModel;
import com.jason.workdemo.page.progress.SubscriberOnNextListener;

import java.util.List;

/**
 * Created by liuzhenhui on 2016/11/8.
 */
public class RxRetrofitDemoPresenter extends RxRetrofitContract.Presenter {
    public static final String TAG = RxRetrofitDemoPresenter.class.getSimpleName();

    private GetMovieModel mGetMovieModel;

    public RxRetrofitDemoPresenter(RxRetrofitContract.View view) {
        super(view);
        mGetMovieModel = new GetMovieModel();
        addModel(mGetMovieModel);
    }

    @Override
    public void getTopMovies(int start, int count) {
        //进度条形式监听
        SubscriberOnNextListener listener = new SubscriberOnNextListener<List<Subject>>() {
            @Override
            public void onNext(List<Subject> subjects) {
                showMovies(subjects.toString());
            }
        };
        mGetMovieModel.getMovies(0, 10, listener);

//        //匿名回调
//        mGetMovieModel.getMovies(0, 10).subscribe(new Action1<List<Subject>>() {
//            @Override
//            public void call(List<Subject> subjects) {
//                showMovies(subjects.toString());
//            }
//
//        });
//
//        //实名回调
//        Action1<List<Subject>> action1 = new Action1<List<Subject>>() {
//            @Override
//            public void call(List<Subject> subjects) {
//                showMovies(subjects.toString());
//            }
//        };
//        mGetMovieModel.getMovies(0, 10, action1);
//
//        //原始订阅
//        Subscriber<List<Subject>> subscriber = new Subscriber<List<Subject>>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onNext(List<Subject> subjects) {
//                showMovies(subjects.toString());
//            }
//        };
//        mGetMovieModel.getMovies(0, 10, subscriber);
    }

    public void showMovies(String msg) {
        if (getView() != null) {
            getView().showMovies(msg);
        }
    }
}
