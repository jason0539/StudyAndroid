package com.jason.workdemo.plugin.hook;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by liuzhenhui on 2017/1/6.
 */
public class StubActivity extends Activity{
    public static final String TAG = StubActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView textView = new TextView(this);
        textView.setText("替身Activity");
        setContentView(textView);
    }
}
