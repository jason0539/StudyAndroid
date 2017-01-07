package com.jason.workdemo.plugin.hook;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.IBinder;

import com.jason.common.utils.MLog;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * Created by liuzhenhui on 2016/12/29.
 */
public class HookHelper {
    public static final String TAG = HookHelper.class.getSimpleName();


    /**
     * hook当前activity的mInstrumentation，仅对当前activity有效
     */
    public static void hookActivityInstrumentation(Activity activity) throws Exception {
//        Class<?> activityClass = activity.getClass();
        Class<?> activityClass = Class.forName("android.app.Activity");
        Field mInstrumentationField = activityClass.getDeclaredField("mInstrumentation");
        mInstrumentationField.setAccessible(true);
        Instrumentation mInstrumentation = (Instrumentation) mInstrumentationField.get(activity);
        Instrumentation evilInstrumentation = new EvilInstrumentation(mInstrumentation);
        mInstrumentationField.set(activity, evilInstrumentation);
    }

    /**
     * hook当前应用的activityThread，对当前应用有效，但是时机不够早
     */
    public static void hookActivityThreadInstrumentation() throws Exception {
        //获取当前的ActivityThread对象
        Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
        Method currentActivityThreadMethod = activityThreadClass.getDeclaredMethod("currentActivityThread");
        currentActivityThreadMethod.setAccessible(true);
        //currentActivityThread是一个static方法，可以直接invoke，不需要带实例参数
        Object currentActivityThread = currentActivityThreadMethod.invoke(null);

        // 拿到原始mInstrumentation字段
        Field mInstrumentationField = activityThreadClass.getDeclaredField("mInstrumentation");
        mInstrumentationField.setAccessible(true);
        Instrumentation mInstrumentation = (Instrumentation) mInstrumentationField.get(currentActivityThread);

        //创建代理对象
        Instrumentation evilInstrumentation = new EvilInstrumentation(mInstrumentation);
        mInstrumentationField.set(currentActivityThread, evilInstrumentation);
    }

    public static void hookBinderClipboardService() throws Exception {
        MLog.d(MLog.TAG_HOOK, "HookHelper->" + "hookBinderClipboardService ");
        final String CLIPBOARD_SERVICE = "clipboard";

        // 下面这一段的意思实际就是: ServiceManager.getService("clipboard");
        // 只不过 ServiceManager这个类是@hide的
        Class<?> serviceManager = Class.forName("android.os.ServiceManager");
        Method getService = serviceManager.getDeclaredMethod("getService", String.class);

        // ServiceManager里面管理的原始的Clipboard Binder对象
        // 一般来说这是一个Binder代理对象
        IBinder rawBinder = (IBinder) getService.invoke(null, CLIPBOARD_SERVICE);

        // Hook 掉这个Binder代理对象的 queryLocalInterface 方法
        // 然后在 queryLocalInterface 返回一个IInterface对象, hook掉我们感兴趣的方法即可.
        IBinder hookedBinder = (IBinder) Proxy.newProxyInstance(serviceManager.getClassLoader(),
                new Class[]{IBinder.class}, new BinderProxyHookHandler(rawBinder));

        // 把这个hook过的Binder代理对象放进ServiceManager的cache里面
        // 以后查询的时候 会优先查询缓存里面的Binder, 这样就会使用被我们修改过的Binder了
        Field cacheField = serviceManager.getDeclaredField("sCache");
        cacheField.setAccessible(true);
        Map<String, IBinder> cache = (Map) cacheField.get(null);
        cache.put(CLIPBOARD_SERVICE, hookedBinder);
    }

    public static boolean amsHooked = false;
    //http://weishu.me/2016/03/07/understand-plugin-framework-ams-pms-hook/
    public static void hookAMS() throws Exception {
        if (amsHooked) {
            MLog.d(MLog.TAG_HOOK,"HookHelper->"+"hookAMS ams已经hook，返回");
            return;
        }
        amsHooked = true;
        Class<?> activityManagerNativeClass = Class.forName("android.app.ActivityManagerNative");

        // 获取 gDefault 这个字段, 想办法替换它
        Field gDefaultField = activityManagerNativeClass.getDeclaredField("gDefault");
        gDefaultField.setAccessible(true);
        Object gDefault = gDefaultField.get(null);

        // 4.x以上的gDefault是一个 android.util.Singleton对象; 我们取出这个单例里面的字段
        Class<?> singleton = Class.forName("android.util.Singleton");
        Field mInstanceField = singleton.getDeclaredField("mInstance");
        mInstanceField.setAccessible(true);

        // ActivityManagerNative 的gDefault对象里面原始的 IActivityManager对象
        Object rawIActivityManager = mInstanceField.get(gDefault);

        Class<?> iActivityManagerInterface = Class.forName("android.app.IActivityManager");
//        Thread.currentThread().getContextClassLoader()
        Object proxy = Proxy.newProxyInstance(activityManagerNativeClass.getClassLoader(),
                new Class<?>[]{iActivityManagerInterface}, new AmsAndPmsHookHandler(rawIActivityManager));

        mInstanceField.set(gDefault, proxy);
    }

    public static boolean pmsHooked = false;
    //http://weishu.me/2016/03/07/understand-plugin-framework-ams-pms-hook/
    public static final void hookPMS(Context newBase) throws Exception {
        if (pmsHooked) {
            MLog.d(MLog.TAG_HOOK,"HookHelper->"+"hookPMS 已经hook，直接返回");
            return;
        }
        pmsHooked = true;
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
                new Class[]{iPackageManagerInterface}, new AmsAndPmsHookHandler(sPackageManager));

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
    }
}
