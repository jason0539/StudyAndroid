package com.jason.workdemo.demo.memory;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.jason.workdemo.R;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by daoming.lzh on 2018/9/27
 */
public class MemoryLeakActivity extends Activity {
    static final int ONE_HUND_M = 1024 * 1024 * 100;
    Button btnLeak;
    Button btnCreateBitmap;
    Button btnReleaseBitmap;
    Button btnCreateInnerHeap;
    Button btnReleaseInnerHeap;
    Button btnCreateOuterHeap;
    Button btnReleaseOuterHeap;

    private static final List<Object> leakList = new ArrayList<Object>();
    private List bitmapHolder = new ArrayList();
    private List innerHeapHolder = new ArrayList();
    private List outerHeapHolder = new ArrayList();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_leak);
        btnLeak = (Button) findViewById(R.id.btn_memory_leak);
        btnCreateBitmap = (Button) findViewById(R.id.btn_memory_make_bitmap);
        btnReleaseBitmap = (Button) findViewById(R.id.btn_memory_release_bitmap);
        btnCreateInnerHeap = (Button) findViewById(R.id.btn_memory_make_inner_heap);
        btnReleaseInnerHeap = (Button) findViewById(R.id.btn_memory_release_inner_heap);
        btnCreateOuterHeap = (Button) findViewById(R.id.btn_memory_make_outer_heap);
        btnReleaseOuterHeap = (Button) findViewById(R.id.btn_memory_release_outer_heap);
        btnLeak.setOnClickListener(onClickListener);
        btnCreateBitmap.setOnClickListener(onClickListener);
        btnReleaseBitmap.setOnClickListener(onClickListener);
        btnCreateInnerHeap.setOnClickListener(onClickListener);
        btnReleaseInnerHeap.setOnClickListener(onClickListener);
        btnCreateOuterHeap.setOnClickListener(onClickListener);
        btnReleaseOuterHeap.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_memory_leak:
                    for (int i = 0; i < 100; i++) {
                        //8.0上这种方式不会占用bitmap的实际内存
                        //bitmap.createBitmap
                        Bitmap temp = BitmapFactory.decodeResource(getResources(), R.drawable.set_about_icon2);
                        leakList.add(temp);
                    }
                    break;
                case R.id.btn_memory_make_bitmap:
                    for (int i = 0; i < 100; i++) {
                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.set_about_icon2);
                        bitmapHolder.add(bitmap);
                    }
                    break;
                case R.id.btn_memory_release_bitmap:
                    bitmapHolder.clear();
                    break;
                case R.id.btn_memory_make_inner_heap:
                    byte[] tempBytes = new byte[ONE_HUND_M];
                    for (int i = 0; i < tempBytes.length; i++) {
                        tempBytes[i] = 1;
                    }
                    innerHeapHolder.add(tempBytes);
                    break;
                case R.id.btn_memory_release_inner_heap:
                    innerHeapHolder.clear();
                    break;
                case R.id.btn_memory_make_outer_heap:
                    ByteBuffer bb = ByteBuffer.allocateDirect(ONE_HUND_M);
                    bb.position(ONE_HUND_M-1);
                    IntBuffer intBuffer = bb.asIntBuffer();
                    for (int i = 0; i < ONE_HUND_M/4; i++) {
//                        intBuffer.put(i);
                    }
                    outerHeapHolder.add(bb);
                    break;
                case R.id.btn_memory_release_outer_heap:
                    outerHeapHolder.clear();
                    break;
            }
        }
    };

}
