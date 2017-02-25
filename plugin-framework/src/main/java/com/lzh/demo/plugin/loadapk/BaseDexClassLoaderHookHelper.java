package com.lzh.demo.plugin.loadapk;

import com.jason.common.utils.MLog;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import dalvik.system.DexClassLoader;
import dalvik.system.DexFile;

/**
 * Created by liuzhenhui on 2017/1/10.
 */
public class BaseDexClassLoaderHookHelper {
    public static final String TAG = BaseDexClassLoaderHookHelper.class.getSimpleName();

    public static final void patchClassLoader(ClassLoader classLoader, File apkfile, File optDexFile) {
        try {
            // 获取BaseDexClassLoader : pathList
            Field pathListField = DexClassLoader.class.getSuperclass().getDeclaredField("pathList");
            pathListField.setAccessible(true);
            Object pathList = pathListField.get(classLoader);

            // 获取 PathList: Element[] dexElements
            Field dexElementsField = pathList.getClass().getDeclaredField("dexElements");
            dexElementsField.setAccessible(true);
            Object[] dexElements = (Object[]) dexElementsField.get(pathList);

            // Element 类型
            Class<?> elementClass = dexElements.getClass().getComponentType();

            // 创建一个数组, 用来替换原始的数组
            Object[] newElements = (Object[]) Array.newInstance(elementClass, dexElements.length + 1);

            // 构造插件Element(File file, boolean isDirectory, File zip, DexFile dexFile) 这个构造函数
            Constructor<?> constructor = elementClass.getConstructor(File.class, boolean.class, File.class, DexFile.class);
            Object pluginElement = constructor.newInstance(apkfile, false, apkfile, DexFile.loadDex(apkfile.getCanonicalPath(), optDexFile.getAbsolutePath(), 0));

            Object[] tempArray = new Object[]{pluginElement};
            // 把原始的elements复制进去
            System.arraycopy(dexElements, 0, newElements, 0, dexElements.length);
            // 插件的那个element复制进去
            System.arraycopy(tempArray, 0, newElements, dexElements.length, tempArray.length);

            //替换
            dexElementsField.set(pathList, newElements);
        } catch (Exception e) {
            e.printStackTrace();
            MLog.d(MLog.TAG_HOOK, TAG + "->" + "patchClassLoader " + e.toString());
        }
    }
}
