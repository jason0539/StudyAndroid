package com.jason.workdemo.plugin.loaddex;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.jason.common.utils.MLog;
import com.jason.workdemo.R;

import java.io.File;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

/**
 * liuzhenhui 16/6/13.上午10:41
 */
public class PluginActivity extends Activity {

    private DexClassLoader classLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plugin);
        loadDex();
        findViewById(R.id.btn_invoke_plugin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                invokePlugin();
            }
        });
    }

    private void loadDex() {
        File jarFile = new File("/sdcard/classes.jar");
        if (jarFile.exists()) {
            classLoader = new DexClassLoader(jarFile.toString(), this.getCacheDir().toString(), null,
                    ClassLoader.getSystemClassLoader());
        } else {
            MLog.d("lzh", "PluginActivity-->invokePlugin jar文件不存在");
        }
    }

    private void invokePlugin() {
        if (classLoader != null) {
            try {
                Class<?> c = classLoader.loadClass("Test");
                Method getStringMethod = c.getDeclaredMethod("getTest");
                getStringMethod.setAccessible(true);

                Object instance = c.newInstance();

                String s = (String) getStringMethod.invoke(instance);
                MLog.d("lzh", "PluginActivity-->invokePlugin 反射成功拿到字符串 = " + s);
            } catch (Exception e) {
                MLog.d("lzh", "PluginActivity-->invokePlugin 反射类异常");
                e.printStackTrace();
            }

            try {
                Class<?> c = classLoader.loadClass("Test");
                Method getStringMethod = c.getDeclaredMethod("getTest",int.class);
                getStringMethod.setAccessible(true);

                Object instance = c.newInstance();

                String s = (String) getStringMethod.invoke(instance,Integer.parseInt("2"));
                MLog.d("lzh", "PluginActivity-->invokePlugin 反射成功拿到字符串 = " + s);
            } catch (Exception e) {
                MLog.d("lzh", "PluginActivity-->invokePlugin 反射类异常");
                e.printStackTrace();
            }
        }
    }

}
