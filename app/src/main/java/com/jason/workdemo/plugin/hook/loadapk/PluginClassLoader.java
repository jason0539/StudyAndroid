package com.jason.workdemo.plugin.hook.loadapk;

import com.jason.common.utils.MLog;

import dalvik.system.DexClassLoader;

/**
 * Created by liuzhenhui on 2017/1/8.
 */
public class PluginClassLoader extends DexClassLoader {
    public static final String TAG = PluginClassLoader.class.getSimpleName();

    public PluginClassLoader(String dexPath, String optimizedDirectory, String libraryPath, ClassLoader parent) {
        super(dexPath, optimizedDirectory, libraryPath, parent);
        MLog.d(MLog.TAG_HOOK, TAG + "->" + "PluginClassLoader dexPath = " + dexPath);
    }

    @Override
    protected Class<?> loadClass(String className, boolean resolve) throws ClassNotFoundException {
        return super.loadClass(className, resolve);
    }

    @Override
    public Class<?> loadClass(String className) throws ClassNotFoundException {
        return super.loadClass(className);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        return super.findClass(name);
    }
}
