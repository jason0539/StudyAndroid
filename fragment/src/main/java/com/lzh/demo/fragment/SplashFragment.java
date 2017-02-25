package com.lzh.demo.fragment;

import android.os.Handler;

import com.jason.common.utils.PreferenceUtils;
import com.lzh.demo.fragment.fragment.BaseFragment;
import com.lzh.demo.fragment.fragment.ContentFragment;
import com.lzh.demo.fragment.fragment.JLFragmentManager;
import com.lzh.demo.fragment.mvp.BasePresenter;

/**
 * Created by liuzhenhui on 2016/12/16.
 */
public class SplashFragment extends ContentFragment {
    public static final String TAG = SplashFragment.class.getSimpleName();

    public static final int GO_HOME_DELAY = 700;

    private static Handler mHandler = new Handler();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_splash;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void onInitView() {
    }

    Runnable goHomeRunnable = new Runnable() {
        @Override
        public void run() {
            getJLFragmentManager().showFragment(JLFragmentManager.TYPE_HOME, null, false);
        }
    };

    Runnable initLibrarySdk = new Runnable() {
        @Override
        public void run() {
            PreferenceUtils.init(BaseFragment.getMainActivity());
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        mHandler.postDelayed(goHomeRunnable, GO_HOME_DELAY);
        mHandler.post(initLibrarySdk);
    }
}