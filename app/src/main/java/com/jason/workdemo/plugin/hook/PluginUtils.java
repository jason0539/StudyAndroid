package com.jason.workdemo.plugin.hook;

import android.content.Context;

import com.jason.common.utils.FileUtils;
import com.jason.common.utils.MLog;

import java.io.File;

/**
 * Created by liuzhenhui on 2017/1/7.
 */
public class PluginUtils {
    public static final String TAG = PluginUtils.class.getSimpleName();

    private static Context mContext;

    public static final void init(Context context) {
        mContext = context;
    }

    public static final void copyPluginApk(Context context, String fileName) {
        MLog.d(MLog.TAG_HOOK, TAG + "->" + "copyPluginApk ");
        // 目录/data/user/0等同于/data/data/
        File destPath = context.getFileStreamPath(fileName);
        FileUtils.extractAssetsFile(context, fileName, destPath.getAbsolutePath());
    }

    /**
     * 待加载插件经过opt优化之后存放odex得路径
     */
    public static final String getPluginOptDexDir(String packageName) {
        String odexPath = getPluginBaseDir(packageName) + File.separator + "odex";
        FileUtils.ensureDirectoryExits(odexPath);
        return odexPath;
    }

    /**
     * 插件得lib库路径, 这个demo里面没有用
     */
    public static String getPluginLibDir(String packageName) {
        String libPath = getPluginBaseDir(packageName) + File.separator + "lib";
        FileUtils.ensureDirectoryExits(libPath);
        return libPath;
    }

    // 需要加载得插件得基本目录 /data/data/<package>/files/plugin/<plugin-package-name>
    private static final String getPluginBaseDir(String packageName) {
        String pluginBasePath = mContext.getFileStreamPath("plugin").getAbsolutePath() + File.separator + packageName;
        FileUtils.ensureDirectoryExits(pluginBasePath);
        return pluginBasePath;
    }
}
