package com.jason.workdemo.page.demo.demoretrofit;


import com.jason.workdemo.page.mvp.BasePresenter;
import com.jason.workdemo.page.mvp.BaseView;

/**
 * Created by liuzhenhui on 2016/11/8.
 */
public class RxRetrofitContract {
    public static final String TAG = RxRetrofitContract.class.getSimpleName();

    public interface View extends BaseView<Presenter> {
        public void showMovies(String movie);
    }

    public static abstract class Presenter extends BasePresenter<View> {
        public Presenter(View view) {
            super(view);
        }

        public abstract void getTopMovies(int start, int count);
    }
}
