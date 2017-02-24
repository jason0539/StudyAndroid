package com.jason.workdemo.opengl;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.jason.common.utils.EmulatorUtils;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES10.glClear;
import static android.opengl.GLES10.glClearColor;
import static android.opengl.GLES20.glViewport;
import static javax.microedition.khronos.opengles.GL10.GL_COLOR_BUFFER_BIT;

/**
 * Created by liuzhenhui on 2017/2/24.
 */

public class FirstOpenGLProjectActivity extends Activity {

    private GLSurfaceView glSurfaceView;
    private boolean renderSet = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        glSurfaceView = new GLSurfaceView(this);
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        boolean supportES2 = configurationInfo.reqGlEsVersion >= 0x20000;

        if (supportES2 || EmulatorUtils.isEmulator()) {
            glSurfaceView.setEGLContextClientVersion(2);
            glSurfaceView.setRenderer(new FirstOpenGLProjectRender());
            renderSet = true;
        } else {
            Toast.makeText(this, "不支持opengl2.0", Toast.LENGTH_SHORT).show();
            return;
        }
        setContentView(glSurfaceView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (renderSet) {
            glSurfaceView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (renderSet) {
            glSurfaceView.onPause();
        }
    }

    class FirstOpenGLProjectRender implements GLSurfaceView.Renderer {
        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            glClearColor(1.0f, 0.0f, 0.0f, 0.0f);
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            glViewport(0, 0, width, height);
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            glClear(GL_COLOR_BUFFER_BIT);
        }
    }
}
