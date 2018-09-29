package com.jason.workdemo.demo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.jason.common.utils.FileUtils;
import com.jason.workdemo.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by daoming.lzh on 2018/9/28
 */
public class BitmapDemoActivity extends Activity {
    public static final String PATH_FILE_TEMP = Environment.getExternalStorageDirectory() + "/test.jpg.temp";
    public static final String PATH_FILE_JPG = Environment.getExternalStorageDirectory() + "/test.jpg";
    Bitmap testBitmap;

    Button btnPixel;
    ImageView ivPixel;

    Button btnDecode;
    ImageView ivDecode;

    Button btnSave;
    Button btnCompress;
    ImageView ivCompress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bitmap);
        btnPixel = (Button) findViewById(R.id.btn_bitmap_pixel);
        ivPixel = (ImageView) findViewById(R.id.iv_bitmap_pixel);
        btnDecode = (Button) findViewById(R.id.btn_bitmap_decode);
        ivDecode = (ImageView) findViewById(R.id.iv_bitmap_decode);
        btnSave = (Button) findViewById(R.id.btn_bitmap_save);
        btnCompress = (Button) findViewById(R.id.btn_bitmap_compress);
        ivCompress = (ImageView) findViewById(R.id.iv_bitmap_compress);
        try {
            testBitmap = BitmapFactory.decodeStream(getResources().getAssets().open("test.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        btnPixel.setOnClickListener(onClickListener);
        btnDecode.setOnClickListener(onClickListener);
        btnSave.setOnClickListener(onClickListener);
        btnCompress.setOnClickListener(onClickListener);
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
                case R.id.btn_bitmap_save:
                    byte[] bitmapBytes = getBitmapByteByCopyPixel(testBitmap);
                    FileUtils.writeByteToFile(bitmapBytes, PATH_FILE_TEMP);
                    break;
                case R.id.btn_bitmap_compress:
                    byte[] oriByte = FileUtils.readByteFromFile(PATH_FILE_TEMP);
                    ByteBuffer wrap = ByteBuffer.wrap(oriByte);
                    Bitmap temp = Bitmap.createBitmap(testBitmap.getWidth(),testBitmap.getHeight()/2, Bitmap.Config.ARGB_8888);
                    temp.copyPixelsFromBuffer(wrap);
                    byte[] dstByte = getBytesByBitmapCompress(temp); // 压缩
                    FileUtils.writeByteToFile(dstByte,PATH_FILE_JPG);
                    break;
            }
        }
    };

    // byte[] -> bitmap
    public static Bitmap getBitmapByOriginBytes(byte[] bytes, int w, int h, Bitmap.Config config) {
        if (bytes == null || bytes.length == 0 || w * h > bytes.length) return null;
        int[] ints = bytesToInts(bytes);
        Bitmap bm = Bitmap.createBitmap(ints, w, h, config);
        return bm;
    }

    // bitmap -> byte[]
    public static byte[] getBytesByBitmapCompress(Bitmap bitmap) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        //压缩成jpg体积更小
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, output);
        byte[] bytes = output.toByteArray();

        try {
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bytes;
    }

    public static int[] bytesToInts(byte[] bytes) {
        int[] ints = new int[bytes.length / 4];
        ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).asIntBuffer().get(ints);

        return ints;
    }

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

    /**
     * 压缩后copy，byte数组较小
     *
     * @param bitmap
     * @return
     */
    private byte[] getBitmapByteArrayByStream(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * 未压缩直接copy，byte数组较大
     *
     * @param bitmap
     * @return
     */
    public static final byte[] getBitmapByteByCopyPixel(Bitmap bitmap) {
        ByteBuffer buffer = ByteBuffer.allocate(bitmap.getByteCount());
        bitmap.copyPixelsToBuffer(buffer);
        return buffer.array();
    }
}
