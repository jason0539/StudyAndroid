package com.jason.workdemo.page.demo.demoretrofit;

import android.widget.Button;
import android.widget.TextView;

import com.jason.workdemo.R;
import com.jason.workdemo.page.demo.demoretrofit.presenter.RxRetrofitDemoPresenter;
import com.jason.workdemo.page.fragment.ContentFragment;
import com.jason.workdemo.page.mvp.BasePresenter;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by liuzhenhui on 2016/10/27.
 */
public class RxRetrofitFragment extends ContentFragment implements RxRetrofitContract.View {
    public static final String TAG = RxRetrofitFragment.class.getSimpleName();

    @BindView(R.id.btn_rxretrofit_click)
    Button btnClick;
    @BindView(R.id.tv_rxretrofit_result)
    TextView tvResult;

    private RxRetrofitDemoPresenter mPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_rxretrofit;
    }

    @Override
    protected BasePresenter createPresenter() {
        mPresenter = new RxRetrofitDemoPresenter(this);
        return mPresenter;
    }

    @Override
    protected void onInitView() {

    }

    @OnClick(R.id.btn_rxretrofit_click)
    public void clickTestNet() {
        mPresenter.getTopMovies(0, 10);
    }

    @Override
    public void showMovies(String movie) {
        tvResult.setText(movie);
    }
}
