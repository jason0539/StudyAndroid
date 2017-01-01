package com.jason.workdemo.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jason.common.utils.LayoutUtils;
import com.jason.common.utils.ScreenUtils;
import com.jason.workdemo.R;

/**
 * Created by liuzhenhui on 2016/12/8.
 */
public class TextViewActivity extends Activity {
    public static final String TAG = TextViewActivity.class.getSimpleName();
    TextView mTvDemo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout linearLayout = LayoutUtils.getVerticalLinearLayout(this);
        ViewGroup.LayoutParams layoutParams = LayoutUtils.getLayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ScreenUtils.dpToPxInt(this, 50));
        mTvDemo = new TextView(this);
        mTvDemo.setLayoutParams(layoutParams);
        mTvDemo.setText("关注");
        mTvDemo.setGravity(Gravity.CENTER);
        mTvDemo.setBackground(getResources().getDrawable(R.drawable.selector_focus));
        mTvDemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTvDemo.isSelected()) {
                    mTvDemo.setSelected(false);
                    mTvDemo.setText("关注");
                } else {
                    mTvDemo.setSelected(true);
                    mTvDemo.setText("已关注");
                }
            }
        });
        linearLayout.addView(mTvDemo);
        setContentView(linearLayout);
    }
}
