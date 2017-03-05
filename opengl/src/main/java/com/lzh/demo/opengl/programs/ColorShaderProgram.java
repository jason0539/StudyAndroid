package com.lzh.demo.opengl.programs;

import android.content.Context;

import com.lzh.demo.opengl.R;

import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniformMatrix4fv;

/**
 * Created by liuzhenhui on 2017/3/1.
 */

public class ColorShaderProgram extends ShaderProgram {

    private final int uMatrixLocation;

    //Attribute locations
    private final int aPositionLocation;
    private final int aColorLocation;

    public ColorShaderProgram(Context context) {
        super(context, R.raw.simple_vertex_shader, R.raw.simple_fragment_shader);
        //Retrieve uniform locations for the shader program（变换矩阵，顶点着色器用于与实际顶点做运算，实现视角变换效果）
        uMatrixLocation = glGetUniformLocation(program, U_MATRIX);
        //Retrieve attribute locations for the shader program（原始坐标数据）
        aPositionLocation = glGetAttribLocation(program, A_POSITION);
        //（原始图片数据）
        aColorLocation = glGetAttribLocation(program, A_COLOR);
    }

    public void setUniforms(float[] matrix) {
        //Pass the matrix into the shader program
        glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);
    }

    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }

    public int getColorAttributeLocation() {
        return aColorLocation;
    }
}
