package com.lzh.demo.opengl.objects;

import com.lzh.demo.opengl.data.VertexArray;
import com.lzh.demo.opengl.programs.ColorShaderProgram;

import static android.opengl.GLES20.glDrawArrays;
import static com.lzh.demo.opengl.Constants.BYTES_PER_FLOAT;
import static javax.microedition.khronos.opengles.GL10.GL_POINTS;

/**
 * Created by liuzhenhui on 2017/3/1.
 */

public class Mallet {
    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int COLOR_COMPONENT_COUNT = 3;
    private static final int STRIDE =
            (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT;
    private static final float[] VERTEX_DATA = {
            //order of coordinates: X,Y,R,G,B
            0f, -0.4f, 0f, 0f, 1f,
            0f, 0.4f, 1f, 0f, 0f
    };
    private final VertexArray vertexArray;

    public Mallet() {
        vertexArray = new VertexArray(VERTEX_DATA);
    }

    public void bindData(ColorShaderProgram colorShaderProgram) {
        vertexArray.setVertexAttribPointer(
                0,
                colorShaderProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT,
                STRIDE
        );
        vertexArray.setVertexAttribPointer(
                POSITION_COMPONENT_COUNT,
                colorShaderProgram.getColorAttributeLocation(),
                COLOR_COMPONENT_COUNT,
                STRIDE
        );
    }

    public void draw() {
        glDrawArrays(GL_POINTS, 0, 2);
    }
}
