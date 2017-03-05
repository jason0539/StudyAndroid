package com.lzh.demo.opengl.data;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glVertexAttribPointer;
import static com.lzh.demo.opengl.Constants.BYTES_PER_FLOAT;
import static javax.microedition.khronos.opengles.GL10.GL_FLOAT;

/**
 * Created by liuzhenhui on 2017/2/28.
 */

public class VertexArray {
    //用于本地内存中存储顶点矩阵数据
    private final FloatBuffer floatBuffer;

    public VertexArray(float[] vertexData) {
        floatBuffer = ByteBuffer
                .allocateDirect(vertexData.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(vertexData);
    }

    public void setVertexAttribPointer(int dataOffset, int attributeLocation,
                                       int componentCount, int stride) {
        floatBuffer.position(dataOffset);
        glVertexAttribPointer(attributeLocation, componentCount, GL_FLOAT, false, stride, floatBuffer);
        glEnableVertexAttribArray(attributeLocation);
        floatBuffer.position(0);
    }
}
