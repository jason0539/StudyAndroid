package com.lzh.demo.opengl.programs;

import android.content.Context;

import com.lzh.demo.opengl.R;

import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUniformMatrix4fv;

/**
 * Created by liuzhenhui on 2017/3/1.
 */

public class TextureShaderProgram extends ShaderProgram {

    //Uniform locations
    private final int uMatrixLocation;
    private final int uTextureUnitLocation;

    //Attribute locations
    private final int aPositionLocation;
    private final int aTextureCoordinatesLocation;


    public TextureShaderProgram(Context context) {
        super(context, R.raw.texture_vertex_shader, R.raw.texture_fragment_shader);

        //变换矩阵（顶点着色器使用，把顶点数据根据矩阵做运算，可实现平移旋转等视角变换）
        uMatrixLocation = glGetUniformLocation(program, U_MATRIX);
        //纹理数据（片段着色器使用，用于确定哪个位置渲染什么颜色）
        uTextureUnitLocation = glGetUniformLocation(program, U_TEXTURE_UNIT);

        //顶点坐标（桌子的坐标原始数据）
        aPositionLocation = glGetAttribLocation(program, A_POSITION);
        //纹理坐标（纹理的坐标原始数据）
        aTextureCoordinatesLocation = glGetAttribLocation(program, A_TEXTURE_COORDINATES);
    }

    /**
     * 设置纹理数据，纹理的片段着色器用来渲染颜色
     */
    public void setUniforms(float[] matrix, int textureId) {
        //Pass the matrix into the shader program（顶点着色器使用该矩阵映射实际位置）
        glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);

        //Set the active texture unit to texture unit 0
        glActiveTexture(GL_TEXTURE0);
        //Bind the texture to this unit
        glBindTexture(GL_TEXTURE_2D, textureId);
        //Tell the texture uniform sampler to use this texture in the shader by telling it to read from texture unit 0.
        glUniform1i(uTextureUnitLocation, 0);
    }

    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }

    public int getTextureCoordinatesAttributeLocation() {
        return aTextureCoordinatesLocation;
    }

}
