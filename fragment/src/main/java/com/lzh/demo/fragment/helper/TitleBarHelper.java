package com.lzh.demo.fragment.helper;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzh.demo.fragment.R;
import com.lzh.demo.fragment.fragment.JLFragmentManager;


/**
 * Created by liuzhenhui on 2016/11/24.
 */
public class TitleBarHelper {
    public static final String TAG = TitleBarHelper.class.getSimpleName();

    private ViewGroup mRootView;
    private ImageView ivLeftBack;
    private TextView tvMiddleTitle;

    public TitleBarHelper(View view, final JLFragmentManager jl) {
        findviews(view);
        if (ivLeftBack != null) {
            ivLeftBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (jl != null) {
                        jl.back();
                    }
                }
            });
        }
    }

    private void findviews(View view) {
        //此处以title_bar为准，布局中title id必须为title_bar
        mRootView = (ViewGroup) view.findViewById(R.id.title_bar);
        if (mRootView != null) {
            ivLeftBack = (ImageView) mRootView.findViewById(R.id.iv_title_bar_left_icon);
            tvMiddleTitle = (TextView) mRootView.findViewById(R.id.tv_title_bar_middle_text);
        }
    }

    public void setBackGroundTransparent() {
        if (mRootView != null) {
            mRootView.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    public void setMiddleText(String text) {
        if (tvMiddleTitle != null) {
            tvMiddleTitle.setText(text);
        }
    }

    public void hideMiddleText() {
        if (tvMiddleTitle != null) {
            tvMiddleTitle.setVisibility(View.GONE);
        }
    }

    public void hideLeftBack(){
        if (ivLeftBack != null) {
            ivLeftBack.setVisibility(View.GONE);
        }
    }
}
