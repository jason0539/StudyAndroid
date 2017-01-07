package com.jason.workdemo.plugin.hook;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by liuzhenhui on 2017/1/6.
 */
public class PluginTargetActivity extends Activity{
    public static final String TAG = PluginTargetActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView textView = new TextView(this);
        textView.setText("目标插件Activity，没有在manifest文件注册");
        setContentView(textView);
    }
}
