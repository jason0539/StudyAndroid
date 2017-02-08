package com.jason.workdemo.plugin.hook.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;

import com.jason.common.utils.MLog;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liuzhenhui on 2017/1/14.
 */

public class BroadcastLoadHelper {

    public static final void loadBroadcastReceiver(Context context, File pluginFile, ClassLoader classLoader) {
        try {

            Map<ActivityInfo, List<? extends IntentFilter>> sCache = parseBroadcastReceivers(pluginFile);

            for (ActivityInfo info : sCache.keySet()) {
                MLog.d(MLog.TAG_HOOK, "BroadcastLoadHelper->loadBroadcastReceiver load receiver : " + info.name);
                List<? extends IntentFilter> intentFilters = sCache.get(info);

                for (IntentFilter intentFilter : intentFilters) {
                    BroadcastReceiver receiver = (BroadcastReceiver) classLoader.loadClass(info.name).newInstance();
                    context.registerReceiver(receiver, intentFilter);
                }
            }
        } catch (Exception e) {
            MLog.d(MLog.TAG_HOOK,"BroadcastLoadHelper->loadBroadcastReceiver " + e.toString());
            e.printStackTrace();
        }
    }

    /**
     * 解析Apk文件中的 <receiver>
     */
    public static final Map<ActivityInfo, List<? extends IntentFilter>> parseBroadcastReceivers(File apkFile) {

        Map<ActivityInfo, List<? extends IntentFilter>> sCache = new HashMap<>();

        try {

            Class<?> packageParserClass = Class.forName("android.content.pm.PackageParser");
            Method parsePackageMethod = packageParserClass.getDeclaredMethod("parsePackage", File.class, int.class);

            Object packageParser = packageParserClass.newInstance();

            // parsePackage获取到apk对应的Package对象
            Object packageObj = parsePackageMethod.invoke(packageParser, apkFile, PackageManager.GET_RECEIVERS);

            // 读取Package对象里面的receivers字段,注意这是一个 List<Activity> (没错,底层把<receiver>当作<activity>处理)
            // 接下来要做的就是根据这个List<Activity> 获取到Receiver对应的 ActivityInfo (依然是把receiver信息用activity处理了)
            Field receiversField = packageObj.getClass().getDeclaredField("receivers");
            List receivers = (List) receiversField.get(packageObj);

            // 调用generateActivityInfo 方法, 把PackageParser.Activity 转换成 ActivityInfo
            Class<?> packageParser$ActivityClass = Class.forName("android.content.pm.PackageParser$Activity");

            Class<?> packageUserStateClass = Class.forName("android.content.pm.PackageUserState");
            Class<?> userHandler = Class.forName("android.os.UserHandle");
            Method getCallingUserIdMethod = userHandler.getDeclaredMethod("getCallingUserId");
            int userId = (int) getCallingUserIdMethod.invoke(null);
            Object defaultUserState = packageUserStateClass.newInstance();

            Class<?> componentClass = Class.forName("android.content.pm.PackageParser$Component");
            Field intentsField = componentClass.getDeclaredField("intents");

            // 需要调用 android.content.pm.PackageParser#
            // generateActivityInfo(android.content.pm.ActivityInfo, int, android.content.pm.PackageUserState, int)
            Method generateReceiverInfo = packageParserClass.getDeclaredMethod("generateActivityInfo",
                    packageParser$ActivityClass, int.class, packageUserStateClass, int.class);

            // 解析receiver以及对应的intentFilter
            for (Object receiver : receivers) {
                ActivityInfo info = (ActivityInfo) generateReceiverInfo.invoke(packageParser, receiver, 0, defaultUserState, userId);
                List<? extends IntentFilter> filters = (List<? extends IntentFilter>) intentsField.get(receiver);
                sCache.put(info, filters);
            }

        } catch (Exception e) {
            MLog.d(MLog.TAG_HOOK, "BroadcastLoadHelper->parseBroadcastReceivers " + e.toString());
        }
        return sCache;
    }
}
