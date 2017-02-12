package com.jason.demoplugin;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by liuzhenhui on 2017/2/12.
 */

public class PluginService2 extends Service{
    @Override
    public void onCreate() {
        Log.d("TAG_HOOK", "PluginService2 -> onCreate : 我是插件中的Service");
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        Log.d("TAG_HOOK", "PluginService2 -> onStart : 我是插件中的Service");
        super.onStart(intent, startId);
    }

    @Override
    public void onDestroy() {
        Log.d("TAG_HOOK", "PluginService2 -> onDestroy : 我是插件中的Service");
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
