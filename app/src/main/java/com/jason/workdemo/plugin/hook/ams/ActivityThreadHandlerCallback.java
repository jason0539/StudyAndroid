package com.jason.workdemo.plugin.hook.ams;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.jason.common.utils.MLog;

import java.lang.reflect.Field;

/**
 * Created by liuzhenhui on 2017/1/6.
 */
public class ActivityThreadHandlerCallback implements Handler.Callback {
    public static final String TAG = ActivityThreadHandlerCallback.class.getSimpleName();

    Handler mBase;

    public ActivityThreadHandlerCallback(Handler base) {
        mBase = base;
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            // ActivityThread里面 "LAUNCH_ACTIVITY" 这个字段的值是100
            // 本来使用反射的方式获取最好, 这里为了简便直接使用硬编码
            case 100:
                handleLanuchActivity(msg);
                break;
        }
        mBase.handleMessage(msg);
        return true;
    }

    private void handleLanuchActivity(Message msg) {
        MLog.d(MLog.TAG_HOOK, TAG + "->" + "handleLanuchActivity ");

        // 这里简单起见,直接取出TargetActivity;
        Object obj = msg.obj;
        // 根据源码:
        // 这个对象是 ActivityClientRecord 类型
        // 我们修改它的intent字段为我们原来保存的即可.
/*        switch (msg.what) {
/             case LAUNCH_ACTIVITY: {
/                 Trace.traceBegin(Trace.TRACE_TAG_ACTIVITY_MANAGER, "activityStart");
/                 final ActivityClientRecord r = (ActivityClientRecord) msg.obj;
/
/                 r.packageInfo = getPackageInfoNoCheck(
/                         r.activityInfo.applicationInfo, r.compatInfo);
/                 handleLaunchActivity(r, null);
*/
        try {
            Field intentField = obj.getClass().getDeclaredField("intent");
            intentField.setAccessible(true);
            Intent fakeIntent = (Intent) intentField.get(obj);

            //检查是否有需要替换的intent
            if (fakeIntent.getExtras().containsKey(AmsHookInvocationHandler.EXTRA_TARGET_INTENT)) {
                MLog.d(MLog.TAG_HOOK, TAG + "->" + "handleLanuchActivity 换回实际intent");
                Intent raw = fakeIntent.getParcelableExtra(AmsHookInvocationHandler.EXTRA_TARGET_INTENT);
                fakeIntent.setComponent(raw.getComponent());
            } else {
                MLog.d(MLog.TAG_HOOK, TAG + "->" + "handleLanuchActivity 不需要更换intent，直接启动");
            }
        } catch (Exception e) {
            MLog.d(MLog.TAG_HOOK, TAG + "->" + "handleLanuchActivity " + e.toString());
            e.printStackTrace();
        }
    }
}
