package com.jason.workdemo.page.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jason.common.utils.MLog;
import com.jason.workdemo.page.mvp.BasePresenter;

import butterknife.ButterKnife;

/**
 * Created by liuzhenhui on 2016/11/14.
 */
public abstract class InnerFragment extends BaseFragment {
    public static final String TAG = InnerFragment.class.getSimpleName();

    protected View mContentView;
    private BasePresenter mBasePresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MLog.d(MLog.TAG_FRAGMENT, TAG + "->" + "onCreateView ");
        super.onCreateView(inflater, container, savedInstanceState);

        if (mContentView != null) {
            ViewGroup parent = (ViewGroup) mContentView.getParent();
            if (parent != null) {
                parent.removeView(mContentView);
            }
        } else {
            mContentView = inflater.inflate(getLayoutId(), mContainer, false);
            mBasePresenter = createPresenter();
            ButterKnife.bind(this, mContentView);
        }
        if (mContentView != null) {
            mContentView.setClickable(true);
        }
        onInitView();
        return mContentView;
    }

    protected abstract int getLayoutId();

    protected abstract void onInitView();

    protected abstract BasePresenter createPresenter();

    @Override
    public void onDetach() {
        MLog.d(MLog.TAG_FRAGMENT, TAG + "->" + "onDetach ");
        super.onDetach();
        if (mBasePresenter != null) {
            mBasePresenter.detachView();
        }
    }
}
