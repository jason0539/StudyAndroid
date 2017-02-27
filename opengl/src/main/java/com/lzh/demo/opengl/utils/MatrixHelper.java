package com.lzh.demo.opengl.utils;

/**
 * Created by liuzhenhui on 2017/2/26.
 */

public class MatrixHelper {
    //Android ICS版本才引入了原生perspectiveM方法，这里自己实现一个类似的
    public static void perspectiveM(float[] m, float yFovInDegrees, float aspect, float near, float far) {
        //计算焦距
        final float angleRadians = (float) (yFovInDegrees * Math.PI / 180);
        final float a = (float) (1.0 / Math.tan(angleRadians / 2.0));
        //输出矩阵
        //OpenGL把矩阵数据按照以列为主的顺序存储，意味着我们一次写一列数据
        //前四个值是第一列，下一组四个第二列，以此类推
        m[0] = a / aspect;
        m[1] = 0f;
        m[2] = 0f;
        m[3] = 0f;

        m[4] = 0f;
        m[5] = a;
        m[6] = 0f;
        m[7] = 0f;

        m[8] = 0f;
        m[9] = 0f;
        m[10] = -((far + near) / (far - near));
        m[11] = -1f;

        m[12] = 0f;
        m[13] = 0f;
        m[14] = -((2f * far * near) / (far - near));
        m[15] = 0f;
    }
}
