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

    ImageView ivOrigin;

    Button btnPixel;
    ImageView ivPixel;

    Button btnDecode;
    ImageView ivDecode;

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
        ivOrigin = (ImageView) findViewById(R.id.iv_bitmap_origin);
        btnCompress = (Button) findViewById(R.id.btn_bitmap_compress);
        ivCompress = (ImageView) findViewById(R.id.iv_bitmap_compress);
        try {
            testBitmap = BitmapFactory.decodeStream(getResources().getAssets().open("test.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        ivOrigin.setImageBitmap(testBitmap);
        btnPixel.setOnClickListener(onClickListener);
        btnDecode.setOnClickListener(onClickListener);
        btnCompress.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                //bitmap->compressed byte[]->bitmap
                case R.id.btn_bitmap_decode:
                    //压缩bitmap得到byte[]，得到的byte[]较小
                    byte[] tempByteArray = getBytesByCompressStream(testBitmap);
                    //从byte[]解码再得到bitmap
                    Bitmap bitmap = getBitmapByCompressedBytes(tempByteArray);
                    ivDecode.setImageBitmap(bitmap);
                    break;
                //bitmap->byte[]->bitmap
                case R.id.btn_bitmap_pixel:
                    //不压缩直接获取bitmap到byte[]，得到的byte[]较大
                    byte[] img = getBytesByCopyPixel(testBitmap);
                    //从byte[]恢复bitmap
                    Bitmap faceImg = getBitmapByCopyedBytes(img);
                    ivPixel.setImageBitmap(faceImg);
                    break;
                //bitmap->获取未压缩的byte[]->保存到file
                //未压缩且直接写到文件，效率很高，用于高频率保存，然后后面再压缩成jpg图片
                case R.id.btn_bitmap_compress:
                    //高效率保存bitmap
                    byte[] bitmapBytes = getBytesByCopyPixel(testBitmap);
                    FileUtils.writeByteToFile(bitmapBytes, PATH_FILE_TEMP);
                    //空闲时读取再压缩保存成jpg图片
                    byte[] oriByte = FileUtils.readByteFromFile(PATH_FILE_TEMP);
                    ByteBuffer wraps = ByteBuffer.wrap(oriByte);
                    Bitmap temp = Bitmap.createBitmap(testBitmap.getWidth(), testBitmap.getHeight(), Bitmap.Config.ARGB_8888);
                    temp.copyPixelsFromBuffer(wraps);
                    ivCompress.setImageBitmap(temp);
                    //压缩后保存
                    byte[] dstByte = getBytesByCompressStream(temp);
                    FileUtils.writeByteToFile(dstByte, PATH_FILE_JPG);
                    //另一种从byte创建bitmap的思路，需要注意byte数组顺序
//                    int imgWidth = testBitmap.getWidth();
//                    int imgHeight = testBitmap.getHeight();
//                    Bitmap temp = Bitmap.createBitmap(imgWidth, imgHeight, Bitmap.Config.ARGB_8888);
//
//                    int[] pixel = new int[imgWidth * imgHeight];
//                    for (int i = 0; i < pixel.length; i++) {
//                        int gray = oriByte[i];
//                        pixel[i] = (0xFF << 24) + (gray << 16) + (gray << 8) + gray;
//                    }
//                    temp.setPixels(pixel, 0, imgWidth, 0, 0, imgWidth, imgHeight);

                    break;
            }
        }
    };

    public static int[] bytesToInts(byte[] bytes) {
        int[] ints = new int[bytes.length / 4];
        ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).asIntBuffer().get(ints);

        return ints;
    }

    //    ========================================================================================================================
    //    =====================================================bitmap -> byte[] - > bitmap=============================
    //    ========================================================================================================================

    /**
     * byte[] -> bitmap
     */
    private Bitmap getBitmapByCopyedBytes(byte[] img) {
        int imgWidth = testBitmap.getWidth();
        int imgHeight = testBitmap.getHeight();
        Bitmap faceImg = Bitmap.createBitmap(imgWidth, imgHeight, Bitmap.Config.ARGB_8888);
        ByteBuffer wrap = ByteBuffer.wrap(img);
        faceImg.copyPixelsFromBuffer(wrap);
        return faceImg;
    }

    /**
     * bitmap->byte[]
     */
    public static final byte[] getBytesByCopyPixel(Bitmap bitmap) {
        ByteBuffer buffer = ByteBuffer.allocate(bitmap.getByteCount());
        bitmap.copyPixelsToBuffer(buffer);
        return buffer.array();
    }


    //    ========================================================================================================================
    //    =====================================================bitmap -> compressed byte[] - > bitmap=============================
    //    ========================================================================================================================

    /**
     * bitmap->compressed byte[]
     */
    private byte[] getBytesByCompressStream(Bitmap testBitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //把bitmap压缩到stream中
        testBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        //stream转成byte数组，如此得到的byte数组占用空间小
        return baos.toByteArray();
    }

    /**
     * compressed byte[] -> bitmap
     */
    private Bitmap getBitmapByCompressedBytes(byte[] tempByteArray) {
        return BitmapFactory.decodeByteArray(tempByteArray, 0, tempByteArray.length);
    }
}
