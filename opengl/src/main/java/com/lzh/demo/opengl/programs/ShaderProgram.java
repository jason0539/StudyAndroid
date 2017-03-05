package com.lzh.demo.opengl.programs;

import android.content.Context;

import com.jason.common.utils.FileUtils;
import com.lzh.demo.opengl.utils.ShaderHelper;

import static android.opengl.GLES20.glUseProgram;

/**
 * Created by liuzhenhui on 2017/3/1.
 */

public class ShaderProgram {
    //Uniform constants
    protected static final String U_MATRIX = "u_Matrix";
    protected static final String U_TEXTURE_UNIT = "u_TextureUnit";

    //Attribute constants
    protected static final String A_POSITION = "a_Position";
    protected static final String A_COLOR = "a_Color";
    protected static final String A_TEXTURE_COORDINATES = "a_TextureCoordinates";

    //Shader program
    protected final int program;

    protected ShaderProgram(Context context, int vertextShaderResourceId,
                            int fragmentShaderResourceId) {
        //Compile the shaders and link the program
        program = ShaderHelper.buildProgram(
                FileUtils.readTextFileFromResource(context, vertextShaderResourceId),
                FileUtils.readTextFileFromResource(context, fragmentShaderResourceId)
        );
    }

    public void useProgram() {
        //Set the current OpenGL shader program to this program
        glUseProgram(program);
    }
}

