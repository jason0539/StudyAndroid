package com.jason.common.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Created by liuzhenhui on 2016/10/28.
 */
public class ImageUtils {
    public static final String TAG = ImageUtils.class.getSimpleName();

    public static Bitmap decodeImage(String imagePath) {
        BitmapFactory.Options localOptions = new BitmapFactory.Options();
        localOptions.inJustDecodeBounds = false;
        Bitmap localBitmap1 = BitmapFactory.decodeFile(imagePath, localOptions);
        return localBitmap1;
    }

    public static Bitmap decodeScaleImage(String imagePath, int outWidth, int outHeight) {
        BitmapFactory.Options localOptions = new BitmapFactory.Options();
        localOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, localOptions);
        int i = calculateInSampleSize(localOptions, outWidth, outHeight);
        localOptions.inSampleSize = i;
        localOptions.inJustDecodeBounds = false;
        Bitmap localBitmap1 = BitmapFactory.decodeFile(imagePath, localOptions);
        return localBitmap1;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
        if (bitmap == null) {
            return null;
        }
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    public static boolean bitmap2File(Bitmap bitmap, File imageFile) {
        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            boolean isOK = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
            return isOK;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
