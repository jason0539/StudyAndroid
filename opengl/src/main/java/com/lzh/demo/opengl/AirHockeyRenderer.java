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
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.orthoM;
import static javax.microedition.khronos.opengles.GL10.GL_COLOR_BUFFER_BIT;
import static javax.microedition.khronos.opengles.GL10.GL_FLOAT;
import static javax.microedition.khronos.opengles.GL10.GL_LINES;
import static javax.microedition.khronos.opengles.GL10.GL_TRIANGLE_FAN;

/**
 * Created by liuzhenhui on 2017/2/24.
 */

public class AirHockeyRenderer implements GLSurfaceView.Renderer {

    private Context mContext;

    //坐标纬度（都是二维坐标）
    private static final int POSITION_COMPONENT_COUNT = 2;
    //颜色纬度
    private static final int COLOR_COMPONENT_COUNT = 3;
    //每个float占用的字节长度
    private static final int BYTES_PER_FLOAT = 4;

    //程序对象
    private int program;

    //顶点着色器中的顶点颜色
    private static final String A_COLOR = "a_Color";
    //记录颜色varying在程序对象中的位置
    private int aColorLocation;
    //跨距（顶点坐标每个坐标所占字节数）
    private static final int STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT;

    //顶点着色器中定义的位置属性
    private static final String A_POSITION = "a_Position";
    //记录位置属性在程序对象中的位置
    private int aPositionLocation;

    //本地内存缓存区，存储顶点位置
    private FloatBuffer vertexData;

    private static final String U_MATRIX = "u_Matrix";
    private int uMatrixLocation;
    //存放正交投影矩阵计算结果
    private final float[] projectionMatrix = new float[16];


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
                //使用三角形扇，绘制四个三角形组成的桌子
                //GL_TRIANGLE_FAN Fan
                0, 0, 1f, 1f, 1f,//X,Y,R,G,B
                -0.5f, -0.8f, 0.7f, 0.7f, 0.7f,
                0.5f, -0.8f, 0.7f, 0.7f, 0.7f,
                0.5f, 0.8f, 0.7f, 0.7f, 0.7f,
                -0.5f, 0.8f, 0.7f, 0.7f, 0.7f,
                -0.5f, -0.8f, 0.7f, 0.7f, 0.7f,

                // Line 1
                -0.5f, 0f, 1f, 0f, 0f,
                0.5f, 0f, 1f, 0f, 0f,

                // Mallets
                0f, -0.4f, 0f, 0f, 1f,
                0f, 0.4f, 1f, 0f, 0f
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

        //着色器中定义了颜色和位置属性，拿到它们在程序对象中的位置
        aColorLocation = glGetAttribLocation(program, A_COLOR);
        aPositionLocation = glGetAttribLocation(program, A_POSITION);
        uMatrixLocation = glGetUniformLocation(program, U_MATRIX);

        //关联属性与顶点数据的数组p38
        //移动缓冲区内部指针到开头，确保从头开始读取位置数据
        vertexData.position(0);
        //告诉OpenGL，可以在vertexData中找到a_Position对应的数据
        //通过这个方法，把 需要绘制的坐标 和 顶点着色器程序中的位置属性 关联了起来，
        //即告诉了OpenGL，它可以从vertexData找到属性a_Position的数据
        glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GL_FLOAT, false, STRIDE, vertexData);
        //为位置属性使能顶点属性数组
        glEnableVertexAttribArray(aPositionLocation);

        //移动缓冲区内部指针到颜色属性开始的地方，即位置属性结束的地方
        vertexData.position(POSITION_COMPONENT_COUNT);
        //关联着色器中的 a_Color 和 颜色数据
        //STRIDE是跨距，告诉OpenGL每个颜色之间有多少个字节
        glVertexAttribPointer(aColorLocation, COLOR_COMPONENT_COUNT, GL_FLOAT, false, STRIDE, vertexData);
        //为颜色属性使能定点属性数组
        glEnableVertexAttribArray(aColorLocation);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport(0, 0, width, height);
        final float aspectRatio = width > height?
                (float)width/(float)height:(float)height/(float)width;
        if (width > height) {
            //横屏，对left、right做变换
            //计算正交矩阵，projectionMatrix存放返回结果
            orthoM(projectionMatrix, 0, -aspectRatio, aspectRatio, -1f,1f,-1f,1f);
        }else {
            //竖屏，对top、bottom做变换
            orthoM(projectionMatrix, 0, -1f, 1f, -aspectRatio, aspectRatio,-1f,1f);
        }
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        glClear(GL_COLOR_BUFFER_BIT);

        //把正交矩阵传递给着色器的u_Matrix
        glUniformMatrix4fv(uMatrixLocation, 1, false, projectionMatrix, 0);

        //第一个参数表示绘制的形状，第二个参数表示从顶点数组的开头处开始读定点，第三个参数表示读入6个顶点
        //绘制三角形扇
        glDrawArrays(GL_TRIANGLE_FAN, 0, 6);

        //绘制分割线
        glDrawArrays(GL_LINES, 6, 2);

        glDrawArrays(GL_POINTS, 8, 1);

        glDrawArrays(GL_POINTS, 9, 1);

    }
}