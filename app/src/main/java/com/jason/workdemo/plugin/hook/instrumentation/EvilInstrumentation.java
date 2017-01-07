package com.jason.workdemo.plugin.hook.instrumentation;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import com.jason.common.utils.MLog;

import java.lang.reflect.Method;

/**
 * Created by liuzhenhui on 2016/12/29.
 */
public class EvilInstrumentation extends Instrumentation {
    public static final String TAG = EvilInstrumentation.class.getSimpleName();

    // ActivityThread中的原始对象，保存起来
    Instrumentation mBase;

    public EvilInstrumentation(Instrumentation base) {
        mBase = base;
    }

    public ActivityResult execStartActivity(Context who, IBinder contextThread, IBinder token, Activity target, Intent intent, int requestCode, Bundle options) {
        MLog.d(MLog.TAG_HOOK, "EvilInstrumentation->" + "execStartActivity 执行了startActivity，参数如下: \n" + "who = [" + who + "], " +
                "\ncontextThread = [" + contextThread + "], \ntoken = [" + token + "], " +
                "\ntarget = [" + target + "], \nintent = [" + intent +
                "], \nrequestCode = [" + requestCode + "], \noptions = [" + options + "]");

        // 开始调用原始的方法, 调不调用随你,但是不调用的话, 所有的startActivity都失效了.
        // 由于这个方法是隐藏的,因此需要使用反射调用;首先找到这个方法
        try {
            Method execStartActivity = Instrumentation.class.getDeclaredMethod("execStartActivity", Context.class, IBinder.class, IBinder.class, Activity.class, Intent.class, int.class, Bundle.class);
            execStartActivity.setAccessible(true);
            return (ActivityResult) execStartActivity.invoke(mBase, who, contextThread, token, target, intent, requestCode, options);
        } catch (ActivityNotFoundException e) {
            MLog.d(MLog.TAG_HOOK, "EvilInstrumentation->" + "execStartActivity activityNotFound :\n" + e.getCause().getMessage());
            throw new RuntimeException("Activity Not Found");
        } catch (Exception e) {
            MLog.d(MLog.TAG_HOOK, "EvilInstrumentation->" + "execStartActivity ,e = " + e.getCause().getMessage());
            // 某该死的rom修改源码，需要适配，或者出现了什么别的异常，
            throw new RuntimeException("Exception happened,please check the detail");
        }
    }
}
