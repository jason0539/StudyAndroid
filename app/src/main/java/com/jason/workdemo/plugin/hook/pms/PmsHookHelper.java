package com.jason.workdemo.plugin.hook.pms;

import android.content.Context;
import android.content.pm.PackageManager;

import com.jason.common.utils.MLog;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by liuzhenhui on 2017/1/7.
 */
public class PmsHookHelper {
    public static final String TAG = PmsHookHelper.class.getSimpleName();

    private static boolean pmsHooked = false;

    /**
     * hook当前应用的PMS
     * http://weishu.me/2016/03/07/understand-plugin-framework-ams-pms-hook/
     */
    public static final void hookPMS(Context newBase) {
        if (pmsHooked) {
            MLog.d(MLog.TAG_HOOK, TAG+"->" + "hookPMS 已经hook，直接返回");
            return;
        }
        pmsHooked = true;
        try {
            // 获取全局的ActivityThread对象
            Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
            Method currentActivityThreadMethod = activityThreadClass.getDeclaredMethod("currentActivityThread");
            Object currentActivityThread = currentActivityThreadMethod.invoke(null);

            // 获取ActivityThread里面原始的 sPackageManager
            Field sPackageManagerField = activityThreadClass.getDeclaredField("sPackageManager");
            sPackageManagerField.setAccessible(true);
            Object sPackageManager = sPackageManagerField.get(currentActivityThread);

            // 准备好代理对象, 用来替换原始的对象
            Class<?> iPackageManagerInterface = Class.forName("android.content.pm.IPackageManager");
            Object amsProxy = Proxy.newProxyInstance(iPackageManagerInterface.getClassLoader(),
                    new Class[]{iPackageManagerInterface}, new PmsHookInvocationHandler(sPackageManager));

            // 1. 替换掉ActivityThread里面的 sPackageManager 字段
            sPackageManagerField.set(currentActivityThread, amsProxy);

            // 2. 替换 ApplicationPackageManager里面的 mPm对象
            PackageManager packageManager = newBase.getPackageManager();
            Field mPMField = packageManager.getClass().getDeclaredField("mPM");
            mPMField.setAccessible(true);
            mPMField.set(packageManager, amsProxy);

            //me：以下同样可用
//        Class<?> applicationPackageManagerClass = Class.forName("android.app.ApplicationPackageManager");
//        Field mPMField = applicationPackageManagerClass.getDeclaredField("mPM");
//        mPMField.setAccessible(true);
//        Object mPm = mPMField.get(packageManager);
//        Object pmsProxy = Proxy.newProxyInstance(iPackageManagerInterface.getClassLoader(),
//                new Class[]{iPackageManagerInterface}, new AmsAndPmsHookHandler(mPm));
//        mPMField.set(packageManager,pmsProxy);
        } catch (Exception e) {
            pmsHooked = false;
            MLog.d(MLog.TAG_HOOK, TAG+"->" + "hookPMS " + e.toString());
        }

    }
}
