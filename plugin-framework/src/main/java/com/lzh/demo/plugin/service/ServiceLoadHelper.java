package com.lzh.demo.plugin.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.os.Binder;
import android.os.IBinder;

import com.jason.common.utils.MLog;
import com.lzh.demo.plugin.ams.AmsHookInvocationHandler;

import java.io.File;
import java.lang.ref.SoftReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liuzhenhui on 2017/2/12.
 */

public class ServiceLoadHelper {
    private static SoftReference<Context> mContext;

    //存储插件的Service信息
    private static final Map<ComponentName, ServiceInfo> mServiceInfoMap = new HashMap<>();

    public static final void loadService(Context context, File pluginFile, boolean replacePackageName) {
        mContext = new SoftReference<Context>(context);
        needReplacePackageName = replacePackageName;

        try {
            Class<?> packageParserClass = Class.forName("android.content.pm.PackageParser");
            Method parsePackageMethod = packageParserClass.getDeclaredMethod("parsePackage", File.class, int.class);

            Object packageParser = packageParserClass.newInstance();

            // 首先调用parsePackage获取到apk对象对应的Package对象
            Object packageObj = parsePackageMethod.invoke(packageParser, pluginFile, PackageManager.GET_SERVICES);

            //读取Package对象里面的services字段
            Field servicesField = packageObj.getClass().getDeclaredField("services");
            List services = (List) servicesField.get(packageObj);

            //根据List<Service>获取到Service对应的ServiceInfo

            //调用generateServiceInfo方法，把PackageParser.Service转换成ServiceInfo
            Class<?> packageParser$ServiceClass = Class.forName("android.content.pm.PackageParser$Service");
            Class<?> packageUserStateClass = Class.forName("android.content.pm.PackageUserState");
            Class<?> userHandler = Class.forName("android.os.UserHandle");
            Method getCallingUserIdMethod = userHandler.getDeclaredMethod("getCallingUserId");
            int userId = (int) getCallingUserIdMethod.invoke(null);
            Object defaultUserState = packageUserStateClass.newInstance();

            // 需要调用 android.content.pm.PackageParser#generateServiceInfo(android.content.pm.PackageParser$Service, int, android.content.pm.PackageUserState, int)
            Method generateServiceInfo = packageParserClass.getDeclaredMethod("generateServiceInfo", packageParser$ServiceClass, int.class, packageUserStateClass, int.class);

            // 解析出intent对应的Service组件
            for (Object service : services) {
                ServiceInfo info = (ServiceInfo) generateServiceInfo.invoke(packageParser, service, 0, defaultUserState, userId);
                mServiceInfoMap.put(new ComponentName(info.packageName, info.name), info);
            }
        } catch (Exception e) {
            MLog.d(MLog.TAG_HOOK, "ServiceLoadHelper->loadService " + e.toString());
            e.printStackTrace();
        }

    }


    private static final Map<String, Service> mServiceMap = new HashMap<String, Service>();

    /**
     * 启动某个插件Service; 如果Service还没有启动, 那么会创建新的插件Service
     *
     * @param proxyIntent
     * @param startId
     */
    public static void onStart(Intent proxyIntent, int startId) {
        Intent targetIntent = proxyIntent.getParcelableExtra(AmsHookInvocationHandler.EXTRA_TARGET_INTENT);
        ServiceInfo serviceInfo = findPluginService(targetIntent);
        if (serviceInfo == null) {
            MLog.d(MLog.TAG_HOOK, "ServiceLoadHelper->onStart 没有找到插件中对应的Service");
            return;
        }
        if (!mServiceMap.containsKey(serviceInfo.name)) {
            createProxyService(serviceInfo);
        }
        Service service = mServiceMap.get(serviceInfo.name);
        service.onStart(targetIntent, startId);
    }

    private static ServiceInfo findPluginService(Intent pluginIntent) {
        for (ComponentName componentName : mServiceInfoMap.keySet()) {
            if (componentName.equals(pluginIntent.getComponent())) {
                return mServiceInfoMap.get(componentName);
            }
        }
        return null;
    }

    private static boolean needReplacePackageName = false;

    /**
     * 通过ActivityThread的handleCreateService方法创建出Service对象
     *
     * @param serviceInfo 插件的ServiceInfo
     * @throws Exception
     */
    private static void createProxyService(ServiceInfo serviceInfo) {
        try {
            IBinder token = new Binder();

            //创建CreateServiceData对象，用来传递给ActivityThread的handleCreateService当做参数
            Class<?> createServiceDataClass = Class.forName("android.app.ActivityThread$CreateServiceData");
            Constructor<?> constructor = createServiceDataClass.getDeclaredConstructor();
            constructor.setAccessible(true);
            Object createServiceData = constructor.newInstance();

            //写入我们创建的createServiceData的token字段，ActivityThread的handleCreateService用这个作为key存储Service
            Field tokenField = createServiceDataClass.getDeclaredField("token");
            tokenField.setAccessible(true);
            tokenField.set(createServiceData, token);

            //写入info对象
            if (needReplacePackageName) {
                //这个修改是使用Hook BaseDexClassLoader的方式加载插件时，loadClass的时候，LoadedApk是主程序的ClassLoader
                //如果是CustomClassloader的方式，则不需要替换包名，使用插件包名即可
                serviceInfo.applicationInfo.packageName = AmsHookInvocationHandler.HOST_PACKAGE_NAME;
            }
            Field infoField = createServiceDataClass.getDeclaredField("info");
            infoField.setAccessible(true);
            infoField.set(createServiceData, serviceInfo);

            //写入compatInfo字段
            //获取默认的compatibility配置
            Class<?> compatibilityClass = Class.forName("android.content.res.CompatibilityInfo");
            Field defaultCompatibilityField = compatibilityClass.getDeclaredField("DEFAULT_COMPATIBILITY_INFO");
            Object defaultCompatibility = defaultCompatibilityField.get(null);
            Field compatInfoField = createServiceDataClass.getDeclaredField("compatInfo");
            compatInfoField.setAccessible(true);
            compatInfoField.set(createServiceData, defaultCompatibility);

            Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
            Method currentActivityThreadMethod = activityThreadClass.getDeclaredMethod("currentActivityThread");
            Object currentActivityThread = currentActivityThreadMethod.invoke(null);

            // private void handleCreateService(CreateServiceData data) {
            Method handleCreateServiceMethod = activityThreadClass.getDeclaredMethod("handleCreateService", createServiceDataClass);
            handleCreateServiceMethod.setAccessible(true);

            handleCreateServiceMethod.invoke(currentActivityThread, createServiceData);

            // handleCreateService创建出来的Service对象并没有返回, 而是存储在ActivityThread的mServices字段里面, 这里我们手动把它取出来
            Field mServicesField = activityThreadClass.getDeclaredField("mServices");
            mServicesField.setAccessible(true);
            Map mServices = (Map) mServicesField.get(currentActivityThread);
            Service service = (Service) mServices.get(token);

            // 获取到之后, 移除这个service, 我们只是借花献佛
            mServices.remove(token);

            // 将此Service存储起来
            mServiceMap.put(serviceInfo.name, service);
        } catch (Exception e) {
            e.printStackTrace();
            MLog.d(MLog.TAG_HOOK, "ServiceLoadHelper->createProxyService " + e.toString());
        }
    }

    public static final int stopService(Intent intent) {
        ServiceInfo serviceInfo = findPluginService(intent);
        if (serviceInfo == null) {
            MLog.d(MLog.TAG_HOOK, "ServiceLoadHelper->stopService 没有找到插件中对应的Service");
            return 0;
        }
        Service service = mServiceMap.get(serviceInfo.name);
        if (service == null) {
            MLog.d(MLog.TAG_HOOK, "ServiceLoadHelper->stopService 所有插件Service都已经停止了");
            return 0;
        }
        service.onDestroy();
        mServiceMap.remove(serviceInfo.name);
        if (mServiceMap.isEmpty()) {
            //没有Service了，ProxyService没必要存在了
            MLog.d(MLog.TAG_HOOK, "ServiceLoadHelper->stopService 插件Service都stop了，停止ProxyService");
            Context appContext = mContext.get();
            appContext.stopService(new Intent().setComponent(new ComponentName(appContext.getPackageName(), ProxyService.class.getName())));
        }
        return 1;
    }
}
