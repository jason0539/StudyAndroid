package com.jason.workdemo.view;

import android.app.Activity;
import android.os.Bundle;
import com.jason.workdemo.R;

/**
 * liuzhenhui 16/6/15.上午6:42
 */
public class CustomViewActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        //TODO 自定义一个斜向布局，对角线排列
    }
}
