package com.lzh.demo.fragment;

import android.view.View;
import android.widget.Button;

import com.lzh.demo.fragment.fragment.BaseFragment;
import com.lzh.demo.fragment.fragment.ContentFragment;
import com.lzh.demo.fragment.fragment.JLFragmentManager;
import com.lzh.demo.fragment.mvp.BasePresenter;

import butterknife.BindView;

/**
 * Created by liuzhenhui on 2016/12/16.
 */
public class HomeFragment extends ContentFragment {
    public static final String TAG = HomeFragment.class.getSimpleName();

    @BindView(R.id.btn_home_rxretrofit)
    Button btnRxRetrofit;

    @BindView(R.id.btn_home_glide)
    Button btnGlide;

    @BindView(R.id.btn_home_rxjava)
    Button btnRxJava;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void onInitView() {
        btnRxRetrofit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseFragment.getJLFragmentManager().showFragment(JLFragmentManager.TYPE_RXRTROFIT);
            }
        });
        btnGlide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseFragment.getJLFragmentManager().showFragment(jlFragmentManager.TYPE_GLIDE);
            }
        });

        btnRxJava.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseFragment.getJLFragmentManager().showFragment(jlFragmentManager.TYPE_RXJAVA);
            }
        });
    }
}
