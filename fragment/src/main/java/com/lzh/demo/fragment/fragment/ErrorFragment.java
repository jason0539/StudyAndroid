package com.lzh.demo.fragment.fragment;

import com.lzh.demo.fragment.R;
import com.lzh.demo.fragment.mvp.BasePresenter;

/**
 * Created by liuzhenhui on 2016/11/7.
 */
public class ErrorFragment extends ContentFragment{
    public static final String TAG = ErrorFragment.class.getSimpleName();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_err;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void onInitView() {

    }
}
