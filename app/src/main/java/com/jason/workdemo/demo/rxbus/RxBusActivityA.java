package com.jason.workdemo.demo.rxbus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.jason.common.rxbus.RxBus;
import com.jason.common.utils.MLog;
import com.jason.workdemo.demo.rxbus.event.TapEvent;

import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by liuzhenhui on 2017/1/18.
 */

public class RxBusActivityA extends Activity {
    TextView textView;
    Subscription subscribe;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        textView = new TextView(this);
        textView.setText("I am A Activity");
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(RxBusActivityA.this, RxBusActivityB.class);
                startActivity(intent);
            }
        });
        setContentView(textView);
        subscribe = RxBus.getInstance().toObserverable().subscribe(new Action1<Object>() {
            @Override
            public void call(Object o) {
                if (o instanceof TapEvent) {
                    MLog.d(MLog.TAG_TEST_RXJAVA, "RxBusActivityA->call tapevent");
                } else {
                    MLog.d(MLog.TAG_TEST_RXJAVA, "RxBusActivityA->call else");
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscribe != null && !subscribe.isUnsubscribed()) {
            subscribe.unsubscribe();
        }
    }
}
