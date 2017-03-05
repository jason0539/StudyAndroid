package com.lzh.demo.opengl.utils;

import com.jason.common.utils.MLog;

import static android.opengl.GLES20.GL_COMPILE_STATUS;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_LINK_STATUS;
import static android.opengl.GLES20.GL_VALIDATE_STATUS;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glAttachShader;
import static android.opengl.GLES20.glCompileShader;
import static android.opengl.GLES20.glCreateProgram;
import static android.opengl.GLES20.glCreateShader;
import static android.opengl.GLES20.glDeleteProgram;
import static android.opengl.GLES20.glDeleteShader;
import static android.opengl.GLES20.glGetProgramInfoLog;
import static android.opengl.GLES20.glGetProgramiv;
import static android.opengl.GLES20.glGetShaderInfoLog;
import static android.opengl.GLES20.glGetShaderiv;
import static android.opengl.GLES20.glLinkProgram;
import static android.opengl.GLES20.glShaderSource;
import static android.opengl.GLES20.glValidateProgram;

/**
 * Created by liuzhenhui on 2017/2/25.
 */

public class ShaderHelper {

    /**
     * 根据顶点着色器代码，编译顶点着色器
     */
    public static final int compileVertexShader(String shaderCode) {
        return compileShader(GL_VERTEX_SHADER, shaderCode);
    }

    /**
     * 根据片段着色器代码，编译片段着色器
     */
    public static final int compileFragmentShader(String shaderCode) {
        return compileShader(GL_FRAGMENT_SHADER, shaderCode);
    }

    public static final int compileShader(int type, String shaderCode) {
        //1、创建一个着色器对象
        //shaderObjectId就是OpenGL对象的引用，后面想要引用这个对象，就要把这个值传回OpenGL
        final int shaderObjectId = glCreateShader(type);
        //返回0表示对象创建失败
        if (shaderObjectId == 0) {
            MLog.d(MLog.TAG_OPENGL, "ShaderHelper->compileShader counld not create new shader");
            return 0;
        }
        //2、上传和编译着色器代码
        //告诉OpenGl读入shaderCode定义的源代码，与shaderObjectId对应的着色器对象关联起来
        glShaderSource(shaderObjectId, shaderCode);
        //编译着色器
        glCompileShader(shaderObjectId);
        //3、取出编译状态
        final int[] compileStatus = new int[1];
        glGetShaderiv(shaderObjectId, GL_COMPILE_STATUS, compileStatus, 0);
        //4、取出着色器信息日志
        MLog.d(MLog.TAG_OPENGL, "ShaderHelper->compileShader Result of compiling source : " +
                "\n" + shaderCode + "\n" + glGetShaderInfoLog(shaderObjectId));
        //5、验证编译状态并返回着色器对象ID
        if (compileStatus[0] == 0) {
            //失败则删除
            glDeleteShader(shaderObjectId);
            MLog.d(MLog.TAG_OPENGL, "ShaderHelper->compileShader Compilation of shader failed");
            return 0;
        }
        return shaderObjectId;
    }

    /**
     * 把顶点、片段着色器一起链接进程序
     */
    public static int linkProgram(int vertexShaderId, int fragmentShaderId) {
        //新建程序对象
        final int programeObjectId = glCreateProgram();
        if (programeObjectId == 0) {
            MLog.d(MLog.TAG_OPENGL, "ShaderHelper->linkProgram create program failed");
            return 0;
        }
        //附上着色器
        glAttachShader(programeObjectId, vertexShaderId);
        glAttachShader(programeObjectId, fragmentShaderId);
        //链接程序
        glLinkProgram(programeObjectId);
        //检查链接是否成功
        final int[] linkStatus = new int[1];
        glGetProgramiv(programeObjectId, GL_LINK_STATUS, linkStatus, 0);
        MLog.d(MLog.TAG_OPENGL, "ShaderHelper->linkProgram Result of linking program:\n" +
                glGetProgramInfoLog(programeObjectId));
        if (linkStatus[0] == 0) {
            glDeleteProgram(programeObjectId);
            MLog.d(MLog.TAG_OPENGL, "ShaderHelper->linkProgram Linking of program failed");
            return 0;
        }
        return programeObjectId;
    }

    public static boolean validateProgram(int programObjectId) {
        glValidateProgram(programObjectId);
        final int[] validateStatus = new int[1];
        glGetProgramiv(programObjectId, GL_VALIDATE_STATUS, validateStatus, 0);
        MLog.d(MLog.TAG_OPENGL, "ShaderHelper->validateProgram validateStatus = " + validateStatus[0] + "" +
                "\nLog:" + glGetProgramInfoLog(programObjectId));
        return validateStatus[0] != 0;
    }

    public static int buildProgram(String vertexShaderSource, String fragmentShaderSource) {
        int program;

        //Compile the shaders
        int vertexShader = compileVertexShader(vertexShaderSource);
        int fragmentShader = compileFragmentShader(fragmentShaderSource);

        //Link them into a shader program
        program = linkProgram(vertexShader, fragmentShader);
        return program;
    }
}
