package com.jason.workdemo.plugin.hook.contentprovider;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;

import com.jason.common.utils.MLog;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuzhenhui on 2017/2/18.
 */

public class ContentProviderLoadHelper {
    /**
     * 在进程内部安装provider, 也就是调用 ActivityThread.installContentProviders方法
     */
    public static final void installContentProvider(Context context, File pluginApk, boolean needReplacePackageName) {
        MLog.d(MLog.TAG_HOOK,"ContentProviderLoadHelper->installContentProvider ");
        try {
            List<ProviderInfo> providerInfos = parseProviders(pluginApk);

            if (needReplacePackageName) {
                //这个修改是使用Hook BaseDexClassLoader的方式加载插件时，loadClass的时候，LoadedApk是主程序的ClassLoader
                //如果是CustomClassloader的方式，则不需要替换包名，使用插件包名即可
                for (ProviderInfo providerInfo : providerInfos) {
                    providerInfo.applicationInfo.packageName = context.getPackageName();
                }
            }

            //拿到宿主ActivityThread
            Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
            Method currentActivityThreadMethod = activityThreadClass.getMethod("currentActivityThread");
            Object currentActivityThread = currentActivityThreadMethod.invoke(null);

            //调用installContentProviders方法
            Method installContentProvidersMethod = activityThreadClass.getDeclaredMethod("installContentProviders", Context.class, List.class);
            installContentProvidersMethod.setAccessible(true);
            installContentProvidersMethod.invoke(currentActivityThread,context,providerInfos);
        } catch (Exception e) {
            MLog.d(MLog.TAG_HOOK, "ContentProviderLoadHelper->installContentProvider " + e.getCause().toString());
        }
    }

    /**
     * 解析Apk文件中的 <provider>, 并存储起来
     * 主要是调用PackageParser类的generateProviderInfo方法
     */
    private static List<ProviderInfo> parseProviders(File pluginApk) throws Exception {
        Class<?> packageParserClass = Class.forName("android.content.pm.PackageParser");
        Method parsePackageMethod = packageParserClass.getDeclaredMethod("parsePackage", File.class, int.class);

        Object packageParser = packageParserClass.newInstance();

        //调用parsePackage拿到apk对象对应的Package对象
        Object packageObj = parsePackageMethod.invoke(packageParser, pluginApk, PackageManager.GET_PROVIDERS);

        //读取Package对象里面的providers字段
        Field providersField = packageObj.getClass().getDeclaredField("providers");
        List providers = (List) providersField.get(packageObj);

        //调用generateProviderInfo方法，把PackageParser.Provider转换成ProviderInfo
        Class<?> packageParser$ProviderClass = Class.forName("android.content.pm.PackageParser$Provider");
        Class<?> packageUserStateClass = Class.forName("android.content.pm.PackageUserState");
        Class<?> userHandlerClass = Class.forName("android.os.UserHandle");
        Method getCallingUserIdMethod = userHandlerClass.getDeclaredMethod("getCallingUserId");
        int userId = (int) getCallingUserIdMethod.invoke(null);
        Object defaultUserState = packageUserStateClass.newInstance();
        Method generateProviderInfoMethod = packageParserClass.getDeclaredMethod("generateProviderInfo",packageParser$ProviderClass,int.class,packageUserStateClass, int.class);

        List<ProviderInfo> infos = new ArrayList<>();
        for (Object provider : providers) {
            ProviderInfo providerInfo = (ProviderInfo) generateProviderInfoMethod.invoke(packageParser,provider,0,defaultUserState,userId);
            infos.add(providerInfo);
        }

        return infos;
    }
}
