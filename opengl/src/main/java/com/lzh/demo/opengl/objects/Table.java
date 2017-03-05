package com.lzh.demo.opengl.objects;

import com.lzh.demo.opengl.data.VertexArray;
import com.lzh.demo.opengl.programs.TextureShaderProgram;

import static android.opengl.GLES20.glDrawArrays;
import static com.lzh.demo.opengl.Constants.BYTES_PER_FLOAT;
import static javax.microedition.khronos.opengles.GL10.GL_TRIANGLE_FAN;

/**
 * Created by liuzhenhui on 2017/2/28.
 */

public class Table {
    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;
    private static final int STRIDE =
            (POSITION_COMPONENT_COUNT + TEXTURE_COORDINATES_COMPONENT_COUNT) * BYTES_PER_FLOAT;

    private static final float[] VERTEX_DATA = {
            //order of coordinates: X,Y,S,T(因为纹理坐标系t轴向下为正方向，t分量与矩形y分量方向相反p94)
            //Triangle Fan
            0f,    0f, 0.5f, 0.5f,
            -0.5f, -0.8f,   0f, 0.9f,
            0.5f, -0.8f,   1f, 0.9f,
            0.5f,  0.8f,   1f, 0.1f,
            -0.5f,  0.8f,   0f, 0.1f,
            -0.5f, -0.8f,   0f, 0.9f
    };

    private final VertexArray vertexArray;

    public Table() {
        //初始化顶点数据
        vertexArray = new VertexArray(VERTEX_DATA);
    }

    public void bindData(TextureShaderProgram textureProgram) {
        //绑定顶点数组到着色器程序上
        vertexArray.setVertexAttribPointer(
                0,
                textureProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT,
                STRIDE
        );
        vertexArray.setVertexAttribPointer(
                POSITION_COMPONENT_COUNT,
                textureProgram.getTextureCoordinatesAttributeLocation(),
                TEXTURE_COORDINATES_COMPONENT_COUNT,
                STRIDE
        );
    }

    public void draw() {
        glDrawArrays(GL_TRIANGLE_FAN, 0, 6);
    }
}
