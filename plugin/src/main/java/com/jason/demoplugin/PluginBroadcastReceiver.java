package com.jason.demoplugin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by liuzhenhui on 2017/2/7.
 */

public class PluginBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("TAG_HOOK", "PluginBroadcastReceiver -> onReceive : 我是插件PluginBroadcastReceiver，收到广播");
        Toast.makeText(context, "我是插件PluginBroadcastReceiver，收到广播", Toast.LENGTH_SHORT).show();
    }
}
