package com.jason.workdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.jason.workdemo.scheme.SchemeDemoActivity;
import com.jason.workdemo.webview.WebviewActivity;

/**
 * liuzhenhui 16/5/5.下午9:55
 */
public class MainActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_scheme).setOnClickListener(this);
        findViewById(R.id.btn_velocity).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.btn_scheme:
                intent.setClass(MainActivity.this, SchemeDemoActivity.class);
                break;
            case R.id.btn_velocity:
                intent.setClass(MainActivity.this, WebviewActivity.class);
                break;
        }
        startActivity(intent);
    }

}