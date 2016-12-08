package com.jason.workdemo.demo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import com.jason.workdemo.R;

/**
 * liuzhenhui 16/6/15.下午2:00
 */
public class HandlerDemoActivity extends Activity {
    Button mBtnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler);
        mBtnStart = (Button) findViewById(R.id.btn_start_handler);
        mBtnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // 错误用法，非UI线程构造的Handler不能用于更新UI
                        Looper.prepare();
                        Handler handler = new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                mBtnStart.setText("消息啊消息");
                            }
                        };
                        handler.sendEmptyMessage(1);
                        Looper.loop();
                    }
                }).start();
            }
        });
    }
}
