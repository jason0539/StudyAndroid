package com.lzh.demo.opengl;

import android.content.Context;
import android.opengl.GLSurfaceView;

import com.jason.common.utils.FileUtils;
import com.jason.common.utils.MLog;
import com.lzh.demo.opengl.utils.ShaderHelper;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES10.glClear;
import static android.opengl.GLES10.glDrawArrays;
import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.GLES20.glViewport;
import static javax.microedition.khronos.opengles.GL10.GL_COLOR_BUFFER_BIT;
import static javax.microedition.khronos.opengles.GL10.GL_FLOAT;
import static javax.microedition.khronos.opengles.GL10.GL_LINES;
import static javax.microedition.khronos.opengles.GL10.GL_TRIANGLES;

/**
 * Created by liuzhenhui on 2017/2/24.
 */

public class AirHockeyRenderer implements GLSurfaceView.Renderer {

    private Context mContext;

    //坐标纬度（都是二维坐标）
    private static final int POSITION_COMPONENT_COUNT = 2;
    //每个float占用的字节长度
    private static final int BYTES_PER_FLOAT = 4;

    //程序对象
    private int program;

    //片段着色器中定义的颜色uniform
    private static final String U_COLOR = "u_Color";
    //记录颜色uniform在程序对象中的位置
    private int uColorLocation;

    //顶点着色器中定义的位置属性
    private static final String A_POSITION = "a_Position";
    //记录位置属性在程序对象中的位置
    private int aPositionLocation;

    //本地内存缓存区，存储顶点位置
    private FloatBuffer vertexData;


    public AirHockeyRenderer(Context context) {
        mContext = context;
        //设计坐标
//        float[] tableVerticalsWithTriangles = {
//                //triangle 1
//                0f, 0f,
//                9f, 14f,
//                0f, 14f,
//
//                //triangle 2
//                0f, 0f,
//                9, 0f,
//                9f, 14f,
//
//                //line 1
//                0f, 7f,
//                9f, 7f,
//
//                //mallets
//                4.5f, 2f,
//                4.5f, 12f,
//        };
        //对应到OpenGL坐标
        float[] tableVerticalsWithTriangles = {
                // Triangle 1
                -0.5f, -0.5f,
                0.5f, 0.5f,
                -0.5f, 0.5f,

                // Triangle 2
                -0.5f, -0.5f,
                0.5f, -0.5f,
                0.5f, 0.5f,

                // Line 1
                -0.5f, 0f,
                0.5f, 0f,

                // Mallets
                0f, -0.25f,
                0f, 0.25f
        };
        vertexData = ByteBuffer
                //分配本地内存
                .allocateDirect(tableVerticalsWithTriangles.length * BYTES_PER_FLOAT)
                //使用本地字节序
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        //把虚拟机内存中的顶点数组复制到本地内存
        vertexData.put(tableVerticalsWithTriangles);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        //清屏
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        //读取顶点着色器
        String vertexShaderSource = FileUtils.readTextFileFromResource(mContext, R.raw.simple_vertex_shader);
        //读取片段着色器
        String fragmentShaderSource = FileUtils.readTextFileFromResource(mContext, R.raw.simple_fragment_shader);
        //编译顶点着色器对象
        int vertexShader = ShaderHelper.compileVertexShader(vertexShaderSource);
        //编译片段着色器对象
        int fragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderSource);
        //链接程序
        program = ShaderHelper.linkProgram(fragmentShader, vertexShader);
        //验证程序
        if (ShaderHelper.validateProgram(program)) {
            MLog.d(MLog.TAG_OPENGL, "AirHockeyRenderer->onSurfaceCreated 程序验证通过");
        } else {
            MLog.d(MLog.TAG_OPENGL, "AirHockeyRenderer->onSurfaceCreated 程序验证失败");
        }
        //使用程序，告诉OpenGL在绘制任何东西到屏幕上的时候都要使用这里定义的程序
        glUseProgram(program);

        //获取uniform和属性的位置
        //片段着色器中定义了颜色uniform，拿到它在程序对象中的位置
        uColorLocation = glGetUniformLocation(program, U_COLOR);
        aPositionLocation = glGetAttribLocation(program, A_POSITION);

        //关联属性与顶点数据的数组p38
        //移动缓冲区内部指针到开头，确保从头开始读取数据
        vertexData.position(0);
        //告诉OpenGL，可以在vertexData中找到a_Position对应的数据
        //通过这个方法，把 需要绘制的坐标 和 顶点着色器程序中的位置属性 关联了起来，
        //即告诉了OpenGL，它可以从vertexData找到属性a_Position的数据
        glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GL_FLOAT, false, 0, vertexData);
        //使能顶点数组
        glEnableVertexAttribArray(aPositionLocation);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        glClear(GL_COLOR_BUFFER_BIT);

        //更新着色器代码中u_Color的值，白色RGBA
        glUniform4f(uColorLocation, 1.0f, 1.0f, 1.0f, 1.0f);
        //第一个参数表示绘制三角形，第二个参数表示从顶点数组的开头处开始读定点，第三个参数表示读入6个顶点
        glDrawArrays(GL_TRIANGLES, 0, 6);

        //红色
        glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
        //绘制分割线
        glDrawArrays(GL_LINES, 6, 2);

        //蓝色
        glUniform4f(uColorLocation, 0.0f, 0.0f, 1.0f, 1.0f);
        glDrawArrays(GL_POINTS, 8, 1);

        //红色
        glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
        glDrawArrays(GL_POINTS, 9, 1);

    }
}