package com.lzh.demo.opengl;

import android.content.Context;
import android.opengl.GLSurfaceView;

import com.lzh.demo.opengl.objects.Mallet;
import com.lzh.demo.opengl.objects.Table;
import com.lzh.demo.opengl.programs.ColorShaderProgram;
import com.lzh.demo.opengl.programs.TextureShaderProgram;
import com.lzh.demo.opengl.utils.MatrixHelper;
import com.lzh.demo.opengl.utils.TextureHelper;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES10.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES10.glClear;
import static android.opengl.GLES10.glClearColor;
import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.translateM;

/**
 * Created by liuzhenhui on 2017/3/1.
 */

public class AirHockeyRenderer  implements GLSurfaceView.Renderer {
    private final Context context;

    private final float[] projectionMatrix = new float[16];
    private final float[] modelMatrix = new float[16];

    private Table table;
    private Mallet mallet;

    private TextureShaderProgram textureProgram;
    private ColorShaderProgram colorProgram;

    private int texture;

    public AirHockeyRenderer(Context context) {
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        table = new Table();
        mallet = new Mallet();

        textureProgram = new TextureShaderProgram(context);
        colorProgram = new ColorShaderProgram(context);

        texture = TextureHelper.loadTexture(context, R.drawable.air_hockey_surface);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport(0, 0, width, height);
        //以45度的视野创建一个透视投影，视锥体从z值为-1的位置开始，在z值为-10的位置结束
        //perspectiveM(projectionMatrix, 45, (float) width / (float) height, 1f, 10f);
        MatrixHelper.perspectiveM(projectionMatrix, 45, (float) width / (float) height, 1f, 10f);

        //由于桌子默认z轴坐标为0，刚好看到，上面视锥体从-1开始，所以桌子看不到了，需要使用模型矩阵把它移动到视野内
        setIdentityM(modelMatrix, 0);
        //沿着z轴平移-2,把桌子移到视野中
        translateM(modelMatrix, 0, 0f, 0f, -2.5f);
        //x轴旋转60度，实现俯视效果（效果有点奇怪）
        rotateM(modelMatrix, 0, -60, 1f, 0f, 0f);

        // 正交投影矩阵*模型矩阵（顺序不能换p78） 得到一个矩阵，最终乘以这一个矩阵完成正交、平移两个操作
        //临时结果保存
        final float[] temp = new float[16];
        //矩阵相乘
        multiplyMM(temp, 0, projectionMatrix, 0, modelMatrix, 0);
        //结果保存到projectionMatrix（onDrawFrame中使用）
        System.arraycopy(temp, 0, projectionMatrix, 0, temp.length);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        //Clear the rendering surface
        glClear(GL_COLOR_BUFFER_BIT);

        //Draw the table
        textureProgram.useProgram();
        //设置纹理数据
        textureProgram.setUniforms(projectionMatrix, texture);
        //绑定顶点数据
        table.bindData(textureProgram);
        table.draw();

        //Draw the mallets
        colorProgram.useProgram();
        colorProgram.setUniforms(projectionMatrix);
        mallet.bindData(colorProgram);
        mallet.draw();
    }
}
