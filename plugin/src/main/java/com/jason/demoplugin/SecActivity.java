package com.jason.demoplugin;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Date;

/**
 * Created by liuzhenhui on 2017/1/10.
 */
public class SecActivity extends Activity {
    public static final String TAG = SecActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("SecActivity");

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setGravity(Gravity.CENTER_VERTICAL);

        Button sendBroadcast = new Button(this);
        sendBroadcast.setBackgroundColor(Color.parseColor("#FACC2E"));
        sendBroadcast.setText("点击发送广播");
        sendBroadcast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("com.jason.demoplugin.broadcast");
                sendBroadcast(intent);
            }
        });
        linearLayout.addView(sendBroadcast);

        Button startService1 = new Button(this);
        startService1.setBackgroundColor(Color.parseColor("#FACC2E"));
        startService1.setText("启动Service1");
        startService1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(new Intent().setComponent(
                        new ComponentName("com.jason.demoplugin", "com.jason.demoplugin.PluginService1")));
            }
        });
        linearLayout.addView(startService1);

        Button startService2 = new Button(this);
        startService2.setBackgroundColor(Color.parseColor("#FACC2E"));
        startService2.setText("启动Service2");
        startService2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(new Intent().setComponent(
                        new ComponentName("com.jason.demoplugin", "com.jason.demoplugin.PluginService2")));
            }
        });
        linearLayout.addView(startService2);

        Button stopService1 = new Button(this);
        stopService1.setBackgroundColor(Color.parseColor("#FACC2E"));
        stopService1.setText("停止Service1");
        stopService1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(new Intent().setComponent(
                        new ComponentName("com.jason.demoplugin", "com.jason.demoplugin.PluginService1")));
            }
        });
        linearLayout.addView(stopService1);

        Button stopService2 = new Button(this);
        stopService2.setBackgroundColor(Color.parseColor("#FACC2E"));
        stopService2.setText("停止Service2");
        stopService2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(new Intent().setComponent(
                        new ComponentName("com.jason.demoplugin", "com.jason.demoplugin.PluginService2")));
            }
        });
        linearLayout.addView(stopService2);

        Button contentProviderInsert = new Button(this);
        contentProviderInsert.setBackgroundColor(Color.parseColor("#FACC2E"));
        contentProviderInsert.setText("ContentProvider插入");
        contentProviderInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                values.put(PluginContentProvider.PluginTable.PLUGIN_NAME, "plugin com.jason.demoplugin " + new Date(System.currentTimeMillis()).toString());
                getContentResolver().insert(PluginContentProvider.PLUGIN_CONTENT_URI, values);
            }
        });
        linearLayout.addView(contentProviderInsert);

        Button contentProviderQuery = new Button(this);
        contentProviderQuery.setBackgroundColor(Color.parseColor("#FACC2E"));
        contentProviderQuery.setText("ContentProvider查询");
        contentProviderQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = getContentResolver().query(PluginContentProvider.PLUGIN_CONTENT_URI, null, null, null, null);
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        int index = cursor.getColumnIndex(PluginContentProvider.PluginTable.PLUGIN_NAME);
                        if (index != -1) {
                            String pluginName = cursor.getString(index);
                            Log.d("TAG_HOOK", "PluginContentProvider -> query : pluginName = " + pluginName);
                            Toast.makeText(SecActivity.this, "ContentProvider 查询 pluginName = " + pluginName, Toast.LENGTH_SHORT);
                        }
                    }
                    cursor.close();
                }
            }
        });
        linearLayout.addView(contentProviderQuery);

        setContentView(linearLayout);
    }
}
