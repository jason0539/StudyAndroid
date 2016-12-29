package com.jason.workdemo.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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
        setContentView(R.layout.activity_textview);
        mTvDemo = (TextView) findViewById(R.id.tv_textview_demo);
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
    }
}