/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.jason.workdemo.demo.webview;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import com.jason.common.utils.MToast;


/**
 * 解码图片类
 * Created by lvjingxiao on 2015/3/25.
 *
 */
public class ImageDecoder {

    private static final int ATTEMPT_COUNT_TO_DECODE_BITMAP = 3;

    private static final int MAX_TEXTURE_SIZE = getOpengl2MaxTextureSize();

    public static final int IMG_QUALITY = 80;

    /**
     * 将将路径中数据转化成bitmap，如果超出内存限制会进行尺寸的压缩
     * 尝试3次，如果还是失败返回null
     * @param path
     * @return
     */
    public static Bitmap decodeImage(String path) {

        return decodeWithOOMHandling(path);
    }

    private static Bitmap decodeWithOOMHandling(String path) {
        Bitmap result = null;

        BitmapFactory.Options options = getBitmapOptionsForImageDecoding(path);

        for(int attempt = 1; attempt <= ATTEMPT_COUNT_TO_DECODE_BITMAP; attempt++) {
            try {
                result = BitmapFactory.decodeFile(path, options);
            }catch (OutOfMemoryError error) {
                switch (attempt) {
                    case 1:
                        break;
                    case 2:
                        options.inSampleSize = options.inSampleSize + 1;
                        break;
                    case 3:
                        return null;
                }
                continue;
            }
            break;
        }
        return result;
    }

    /**
     * 获取符合要求的options
     * @param path
     * @return
     */
    private static BitmapFactory.Options getBitmapOptionsForImageDecoding(String path) {
        BitmapFactory.Options decodeOptions = new BitmapFactory.Options();
        decodeOptions.inSampleSize = computeImageScale(path);
        decodeOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
        return decodeOptions;
    }

    /**
     * 求得scale值
     * @param path
     * @return
     */
    private static int computeImageScale(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        int scale;

        BitmapFactory.decodeFile(path, options);

        int width = options.outWidth;
        int height = options.outHeight;

        int reqWidth = 0;
        int reqHeight = 0;

		if (width > MAX_TEXTURE_SIZE || height > MAX_TEXTURE_SIZE) {
			reqWidth = width / 2;
			reqHeight = height / 2;
			while (reqWidth > MAX_TEXTURE_SIZE || reqHeight > MAX_TEXTURE_SIZE) {
				reqWidth = reqWidth / 2;
				reqHeight = reqHeight / 2;
			}
		} else {
			return 1;
		}

        scale = calculateInSampleSize(options, reqWidth, reqHeight);
        return scale;
    }

    /**
     * 求得scale值
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            int widthSampleSize = 0;
            int heightSampleSize = 0;
            if (reqWidth < width) {
                widthSampleSize = Math.round((float) width / (float) reqWidth);
            }
            if (reqHeight < height) {
                heightSampleSize = Math.round((float) height / (float) reqHeight);
            }
            inSampleSize = Math.max(widthSampleSize, heightSampleSize);
        }
        return inSampleSize;
    }

    private static int getOpengl2MaxTextureSize() {
        int[] maxTextureSize = new int[1];
        maxTextureSize[0] = 2048;
        android.opengl.GLES20.glGetIntegerv(android.opengl.GLES20.GL_MAX_TEXTURE_SIZE,
                                                   maxTextureSize, 0);
        return maxTextureSize[0];
    }

    /**
     * 根据uri获取实际路径
     * @param context
     * @param uri
     * @return
     */
    public static String getImagePath(Context context, Uri uri) {
        if (uri == null) {
            MToast.show("选取图片异常");
            return null;
        }
        String filename = null;
        if (uri.toString().startsWith("file")) {
            filename = uri.getPath();
        } else {
            String[] pojo = { MediaStore.Images.Media.DATA };
            Cursor cursor = context.getContentResolver()
                                    .query(uri, pojo, null, null, null);
            if (cursor == null) {
                MToast.show("选取图片异常");
                return null;
            }
            if (cursor.moveToFirst() && cursor.getColumnIndex(MediaStore.MediaColumns.DATA) != -1) {
                filename = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));
            } else {
                MToast.show("选取图片异常");
                return null;
            }
            cursor.close();
        }
        return filename;
    }
}
