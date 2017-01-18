package com.jason.workdemo.demo.rxbus;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.jason.common.rxbus.RxBus;
import com.jason.workdemo.demo.rxbus.event.TapEvent;

/**
 * Created by liuzhenhui on 2017/1/18.
 */

public class RxBusActivityB extends Activity{
    TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        textView = new TextView(this);
        textView.setText("I am B Activity");
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxBus.getInstance().send(new TapEvent());
            }
        });
        setContentView(textView);
    }
}
