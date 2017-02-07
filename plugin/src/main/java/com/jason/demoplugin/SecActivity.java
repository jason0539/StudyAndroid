package com.jason.demoplugin;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by liuzhenhui on 2017/1/10.
 */
public class SecActivity extends Activity {
    public static final String TAG = SecActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView textView = new TextView(this);
        textView.setBackgroundColor(Color.parseColor("#FACC2E"));
        textView.setText("Hello,world");
        setContentView(textView);
    }
}
