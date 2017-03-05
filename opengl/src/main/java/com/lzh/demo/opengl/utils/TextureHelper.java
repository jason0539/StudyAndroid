package com.lzh.demo.opengl.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.jason.common.utils.MLog;

import static android.opengl.GLES20.GL_LINEAR;
import static android.opengl.GLES20.GL_LINEAR_MIPMAP_LINEAR;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TEXTURE_MAG_FILTER;
import static android.opengl.GLES20.GL_TEXTURE_MIN_FILTER;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glDeleteTextures;
import static android.opengl.GLES20.glGenTextures;
import static android.opengl.GLES20.glGenerateMipmap;
import static android.opengl.GLES20.glTexParameteri;
import static android.opengl.GLUtils.texImage2D;

/**
 * Created by liuzhenhui on 2017/2/28.
 */

public class TextureHelper {
    public static int loadTexture(Context context, int resourceId) {
        final int[] textureObjectIds = new int[1];
        //生成纹理对象
        glGenTextures(1, textureObjectIds, 0);
        if (textureObjectIds[0] == 0) {
            MLog.d(MLog.TAG_OPENGL, "TextureHelper->loadTexture glGenTexture failed");
            return 0;
        }

        //加载bitmap
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);
        if (bitmap == null) {
            glDeleteTextures(1, textureObjectIds, 0);
            return 0;
        }
        glBindTexture(GL_TEXTURE_2D, textureObjectIds[0]);

        //设置默认的纹理过滤参数
        //缩小使用三线性过滤
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        //放大使用双线性过滤
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        //加载纹理到OpenGL,复制bitmap的位图数据到当前绑定的纹理对象
        texImage2D(GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();

        //生成MIP贴图
        glGenerateMipmap(GL_TEXTURE_2D);

        //解除与这个纹理的绑定，防止用其他纹理方法意外的改变这个纹理对象
        glBindTexture(GL_TEXTURE_2D, 0);

        return textureObjectIds[0];
    }
}
