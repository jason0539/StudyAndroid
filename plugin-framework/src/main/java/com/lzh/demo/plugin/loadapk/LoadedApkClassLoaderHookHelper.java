package com.lzh.demo.plugin.loadapk;

import android.content.pm.ApplicationInfo;

import com.jason.common.utils.MLog;
import com.lzh.demo.plugin.PluginUtils;
import com.lzh.demo.plugin.ams.AmsHookInvocationHandler;

import java.io.File;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liuzhenhui on 2017/1/7.
 */
public class LoadedApkClassLoaderHookHelper {
    public static final String TAG = LoadedApkClassLoaderHookHelper.class.getSimpleName();

    private static Map<String, Object> sLoadedApk = new HashMap<>();
    private static Map<String, ClassLoader> sClassLoader = new HashMap<>();

    public static final void hookLoadedApkInActivityThread(File apkFile) {
        try {
            // 先获取到当前的ActivityThread对象
            Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
            Method currentActivityThreadMethod = activityThreadClass.getDeclaredMethod("currentActivityThread");
            currentActivityThreadMethod.setAccessible(true);
            Object sCurrentActivityThread = currentActivityThreadMethod.invoke(null);

            // 获取到 mPackages 这个静态成员变量, 这里缓存了dex包的信息
            Field mPackagesField = activityThreadClass.getDeclaredField("mPackages");
            mPackagesField.setAccessible(true);
            Map mPackages = (Map) mPackagesField.get(sCurrentActivityThread);

            //调用getPackageInfoNoCheck来生成插件的LoadedApk，需要两个参数ApplicationInfo和CompatibilityInfo
            //有了这两个参数就可以构建出插件的LoadedApk然后放进ActivityThread的mPackage缓存中，这样在加载插件的类时就可以正常加载了

            //构建CompatibilityInfo
            Class<?> compatibilityInfoClass = Class.forName("android.content.res.CompatibilityInfo");
            Field defaultCompatibilityInfoField = compatibilityInfoClass.getDeclaredField("DEFAULT_COMPATIBILITY_INFO");
            defaultCompatibilityInfoField.setAccessible(true);
            Object defaultCompatibilityInfo = defaultCompatibilityInfoField.get(null);

            //构建ApplicationInfo
            ApplicationInfo applicationInfo = generateApplicationInfo(apkFile);

            //生成插件的LoadedApk
            Method getPackageInfoNoCheckMethod = activityThreadClass.getDeclaredMethod("getPackageInfoNoCheck", ApplicationInfo.class, compatibilityInfoClass);
            Object loadedApk = getPackageInfoNoCheckMethod.invoke(sCurrentActivityThread, applicationInfo, defaultCompatibilityInfo);

            //构造插件的classloader，并替换掉LoadedApk的classloader（方便自己处理加载逻辑，这里不替换也可以）
            String odexPath = PluginUtils.getPluginOptDexDir(applicationInfo.packageName);
            String libPath = PluginUtils.getPluginLibDir(applicationInfo.packageName);
            ClassLoader classLoader = new PluginClassLoader(apkFile.getPath(), odexPath, libPath, ClassLoader.getSystemClassLoader());
            Field mClassLoaderField = loadedApk.getClass().getDeclaredField("mClassLoader");
            mClassLoaderField.setAccessible(true);
            mClassLoaderField.set(loadedApk, classLoader);

            // 由于是弱引用, 因此我们必须在某个地方存一份, 不然容易被GC; 那么就前功尽弃了.
            sLoadedApk.put(applicationInfo.packageName, loadedApk);
            sClassLoader.put(applicationInfo.packageName, classLoader);

            //放入ActivityThread的缓存
            WeakReference weakReference = new WeakReference(loadedApk);
            mPackages.put(applicationInfo.packageName, weakReference);

        } catch (Exception e) {
            e.printStackTrace();
            MLog.e(MLog.TAG_HOOK, TAG + "->" + "hookLoadedApkInActivityThread " + e.toString());
        }
    }

    /**
     * 这个方法的最终目的是调用
     * android.content.pm.PackageParser#generateActivityInfo(android.content.pm.PackageParser.Package, int, android.content.pm.PackageUserState)
     */
    private static ApplicationInfo generateApplicationInfo(File apkFile) throws Exception {
        // 找出需要反射的核心类: android.content.pm.PackageParser
        Class<?> packageParserClass = Class.forName("android.content.pm.PackageParser");
        Object packageParser = packageParserClass.newInstance();

        // 拿到我们的目标: generateApplicationInfo方法
        // API 23 !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        // public static ApplicationInfo generateApplicationInfo(Package p, int flags,
        //    PackageUserState state) {
        // 其他Android版本不保证也是如此.
        Class<?> packageParser$PackageClass = Class.forName("android.content.pm.PackageParser$Package");
        Class<?> packageUserStateClass = Class.forName("android.content.pm.PackageUserState");
        Method generateApplicationInfoMethod = packageParserClass.getDeclaredMethod("generateApplicationInfo", packageParser$PackageClass, int.class, packageUserStateClass);

        // 接下来构建调用这个方法需要的参数，分别是PackageParser$Package，int，PackageUserState

        // 首先，构建Package，通过 android.content.pm.PackageParser#parsePackage 这个方法返回得 Package对象得字段获取得到
        Method parsePackageMethod = packageParserClass.getDeclaredMethod("parsePackage", File.class, int.class);
        //第二个参数是解析包使用的flag，我们直接选择解析全部信息，也就是0
        Object packageObj = parsePackageMethod.invoke(packageParser, apkFile, 0);

        // 然后，构建PackageUserState，直接使用默认构造函数构造一个
        Object defaultPackageUserState = packageUserStateClass.newInstance();

        //调用generateApplicationInfo方法
        ApplicationInfo applicationInfo = (ApplicationInfo) generateApplicationInfoMethod.invoke(packageParser, packageObj, 0, defaultPackageUserState);
        String apkPath = apkFile.getPath();

        applicationInfo.sourceDir = apkPath;
        applicationInfo.publicSourceDir = apkPath;

        // API 23为例，在LoadedApk的141行，需要使用applicationInfo.dataDir，原因如下
        // 在使用ContentProvider时，会用到SQLiteOpenHelper，后者默认的数据库目录是通过Context.getDatabasePath得到的，
        // 而这个方法最终正常应该调用的是LoadedApk.getDataDirFile，这个方法返回的变量是LoadedApk构造方法中从applicationInfo.dataDir赋值得到的
        // 如果这里不传，由于dataDir为空，导致LoadedApk.getDataDirFile返回空，最终会使用默认的/data/system作为目录，该目录应用没有权限，最终导致
        // SQLiteDatabase: Failed to open database '/data/system/xxxx.db'
        // android.database.sqlite.SQLiteCantOpenDatabaseException: unknown error (code 14): Could not open database
        applicationInfo.dataDir = "/data/data/"+ AmsHookInvocationHandler.HOST_PACKAGE_NAME;
        return applicationInfo;
    }

    public static final ClassLoader getClassLoader(String packageName) {
        return sClassLoader.get(packageName);
    }
}
