package com.jason.workdemo.page;

import android.view.View;
import android.widget.Button;

import com.jason.workdemo.R;
import com.jason.workdemo.page.fragment.ContentFragment;
import com.jason.workdemo.page.fragment.JLFragmentManager;
import com.jason.workdemo.page.mvp.BasePresenter;

import butterknife.BindView;

/**
 * Created by liuzhenhui on 2016/12/16.
 */
public class HomeFragment extends ContentFragment {
    public static final String TAG = HomeFragment.class.getSimpleName();

    @BindView(R.id.btn_home_rxretrofit)
    Button btnRxRetrofit;

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
                getJLFragmentManager().showFragment(JLFragmentManager.TYPE_RXRTROFIT);
            }
        });
    }
}
