package com.lzh.demo.fragment.demo.demoretrofit;


import com.lzh.demo.fragment.mvp.BasePresenter;
import com.lzh.demo.fragment.mvp.BaseView;

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
