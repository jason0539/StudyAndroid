package com.lzh.demo.fragment.demo.demoglide;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lzh.demo.fragment.R;
import com.lzh.demo.fragment.fragment.ContentFragment;
import com.lzh.demo.fragment.mvp.BasePresenter;

import butterknife.BindView;

/**
 * Created by liuzhenhui on 2016/11/7.
 */
public class DemoGlideFragment extends ContentFragment {
    public static final String TAG = DemoGlideFragment.class.getSimpleName();

    @BindView(R.id.iv_glide_show)
    ImageView ivShow;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_glide;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void onInitView() {
        Glide.with(this).
                load("http://pic3.nipic.com/20090701/2847972_130628068_2.jpg")
                .thumbnail(0.1f)
                .into(ivShow);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
