package com.jason.workdemo.demo.scroller;

import android.app.Activity;
import android.os.Bundle;

import com.jason.workdemo.R;

/**
 * Created by liuzhenhui on 2016/12/8.
 */
public class ScrollerActivity extends Activity{
    public static final String TAG = ScrollerActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroller);
    }
}
