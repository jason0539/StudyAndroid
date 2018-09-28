package com.jason.workdemo.demo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.jason.workdemo.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by daoming.lzh on 2018/9/28
 */
public class BitmapDemoActivity extends Activity {
    Bitmap testBitmap;

    Button btnPixel;
    ImageView ivPixel;
    Button btnDecode;
    ImageView ivDecode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bitmap);
        btnPixel = (Button) findViewById(R.id.btn_bitmap_pixel);
        ivPixel = (ImageView) findViewById(R.id.iv_bitmap_pixel);
        btnDecode = (Button) findViewById(R.id.btn_bitmap_decode);
        ivDecode = (ImageView) findViewById(R.id.iv_bitmap_decode);
        try {
            testBitmap = BitmapFactory.decodeStream(getResources().getAssets().open("test.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        btnPixel.setOnClickListener(onClickListener);
        btnDecode.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_bitmap_decode:
                    byte[] img = getBitmapByteArrayByStream(testBitmap);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
                    ivDecode.setImageBitmap(bitmap);
                    break;
                case R.id.btn_bitmap_pixel:
                    recoverFromPixel();
                    break;
            }
        }
    };

    private void recoverFromPixel() {
        byte[] img = getBitmapByteByCopyPixel(testBitmap);
        int imgWidth = testBitmap.getWidth();
        int imgHeight = testBitmap.getHeight();
        Bitmap faceImg = Bitmap.createBitmap(imgWidth, imgHeight, Bitmap.Config.ARGB_8888);
//
//        int[] pixel = new int[imgWidth * imgHeight];
//        for (int i = 0; i < pixel.length; i++) {
//            int gray = img[i];
//            pixel[i] = (gray << 24) + (gray << 16) + (gray << 8) + gray;
//        }
//        faceImg.setPixels(pixel, 0, imgWidth, 0, 0, imgWidth, imgHeight);
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        faceImg.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        ByteBuffer wrap = ByteBuffer.wrap(img);
        faceImg.copyPixelsFromBuffer(wrap);
        ivPixel.setImageBitmap(faceImg);
    }

    private byte[] getBitmapByteArrayByStream(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    public static final byte[] getBitmapByteByCopyPixel(Bitmap bitmap) {
        ByteBuffer buffer = ByteBuffer.allocate(bitmap.getByteCount());
        bitmap.copyPixelsToBuffer(buffer);
        return buffer.array();
    }
}
