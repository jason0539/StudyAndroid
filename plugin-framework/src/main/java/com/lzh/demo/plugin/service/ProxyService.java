package com.lzh.demo.plugin.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.jason.common.utils.MLog;

/**
 * Created by liuzhenhui on 2017/2/12.
 */

public class ProxyService extends Service {
    @Override
    public void onCreate() {
        MLog.d(MLog.TAG_HOOK, "ProxyService->onCreate ");
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        MLog.d(MLog.TAG_HOOK, "ProxyService->onStart ");
        //分发Service
        ServiceLoadHelper.onStart(intent, startId);
        super.onStart(intent, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        MLog.d(MLog.TAG_HOOK, "ProxyService->onBind ");
        return null;
    }

    @Override
    public void onDestroy() {
        MLog.d(MLog.TAG_HOOK, "ProxyService->onDestroy ");
        super.onDestroy();
    }
}
