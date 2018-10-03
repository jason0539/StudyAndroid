package com.jason.workdemo.demo.memory;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.jason.workdemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daoming.lzh on 2018/9/27
 */
public class MemoryLeakActivity extends Activity {

    Button btnCreate;
    Button btnLeak;

    private static final List<Object> leakList = new ArrayList<Object>();
    private List objectList = new ArrayList();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_leak);
        btnCreate = (Button) findViewById(R.id.btn_memory_make);
        btnLeak = (Button) findViewById(R.id.btn_memory_leak);
        btnCreate.setOnClickListener(onClickListener);
        btnLeak.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_memory_make:
                    for (int i = 0; i < 100; i++) {
                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.set_about_icon2);
                        objectList.add(bitmap);
                    }
                    break;
                case R.id.btn_memory_leak:
                    for (int i = 0; i < 100; i++) {
                        Bitmap temp = Bitmap.createBitmap(300, 300, Bitmap.Config.ARGB_8888);
                        leakList.add(temp);
                    }
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        leakList.clear();
    }
}
