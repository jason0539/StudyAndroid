package com.lzh.demo.plugin;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jason.common.utils.MLog;
import com.jason.common.utils.ScreenUtils;
import com.lzh.demo.plugin.ams.AmsHookHelper;
import com.lzh.demo.plugin.binder.BinderHookHelper;
import com.lzh.demo.plugin.broadcast.BroadcastLoadHelper;
import com.lzh.demo.plugin.contentprovider.ContentProviderLoadHelper;
import com.lzh.demo.plugin.instrumentation.InstrumentationHookHelper;
import com.lzh.demo.plugin.loadapk.BaseDexClassLoaderHookHelper;
import com.lzh.demo.plugin.loadapk.LoadedApkClassLoaderHookHelper;
import com.lzh.demo.plugin.pms.PmsHookHelper;
import com.lzh.demo.plugin.service.ServiceLoadHelper;

import java.io.File;
import java.util.Date;

/**
 * Created by liuzhenhui on 2016/12/29.
 * http://weishu.me/2016/02/16/understand-plugin-framework-binder-hook/
 */
public class HookActivity extends Activity {
    public static final String TAG = HookActivity.class.getSimpleName();

    public static final String PLUGIN_APK_FILE_NAME = "plugin-debug.apk";
    public static final String PLUGIN_PACKAGE_NAME = "com.jason.demoplugin";

    private static final int PATCH_BASE_CLASS_LOADER = 1;
    private static final int CUSTOM_CLASS_LOADER = 2;

    private static final int APK_LOAD_METHOD = PATCH_BASE_CLASS_LOADER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MLog.d(MLog.TAG_HOOK, "HookActivity->" + "onCreate ");
        super.onCreate(savedInstanceState);

        InstrumentationHookHelper.hookActivityInstrumentation(this);

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ScreenUtils.dpToPxInt(this, 50));

        //打开浏览器浏览网页，测试对activity instrument的hook和对AMS的hook
        Button tv = new Button(this);
        tv.setLayoutParams(buttonParams);
        tv.setText("打开百度网址");
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setData(Uri.parse("http://www.baidu.com"));
                // 注意这里使用的ApplicationContext 启动的Activity
                // 因为Activity对象的startActivity使用的并不是ContextImpl的mInstrumentation
                // 而是自己的mInstrumentation, 如果你需要这样, 可以自己Hook
                // 比较简单, 直接替换这个Activity的此字段即可.
                getApplicationContext().startActivity(intent);
                //使用这种方式hook的化，HookHelper.hookActivityInstrumentation(this);
                //则使用下面方式启动
//                startActivity(intent);
            }
        });
        linearLayout.addView(tv);

        //hook剪贴板服务
        Button btnHookClipboard = new Button(this);
        btnHookClipboard.setLayoutParams(buttonParams);
        btnHookClipboard.setText("HookBinder");
        btnHookClipboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BinderHookHelper.hookBinderClipboardService();
            }
        });
        linearLayout.addView(btnHookClipboard);

        //输入框
        EditText etInput = new EditText(this);
        etInput.setLayoutParams(buttonParams);
        linearLayout.addView(etInput);

        //调用PMS方法，测试hook效果
        Button btnHookPMS = new Button(this);
        btnHookPMS.setText("测试PMS效果");
        btnHookPMS.setLayoutParams(buttonParams);
        linearLayout.addView(btnHookPMS);
        btnHookPMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 测试PMS HOOK (调用其相关方法)
                getPackageManager().getInstalledApplications(0);
            }
        });

        //启动没有在manifest文件中注册的activity
        Button btnStartPluginActivity = new Button(this);
        btnStartPluginActivity.setText("启动未注册Activity");
        btnStartPluginActivity.setLayoutParams(buttonParams);
        linearLayout.addView(btnStartPluginActivity);
        btnStartPluginActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HookActivity.this, PluginTargetActivity.class);
                startActivity(intent);
            }
        });

        Button btnLoadPluginApk = new Button(this);
        btnLoadPluginApk.setText("唤起插件Activity");
        btnLoadPluginApk.setLayoutParams(buttonParams);
        linearLayout.addView(btnLoadPluginApk);
        btnLoadPluginApk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                if (APK_LOAD_METHOD == CUSTOM_CLASS_LOADER) {
                    intent.setComponent(new ComponentName(PLUGIN_PACKAGE_NAME, "com.jason.demoplugin.MainActivity"));
                } else if (APK_LOAD_METHOD == PATCH_BASE_CLASS_LOADER) {
                    //这种方式没有处理插件资源的加载，无法使用插件资源
                    intent.setComponent(new ComponentName(PLUGIN_PACKAGE_NAME, "com.jason.demoplugin.SecActivity"));
                }
                startActivity(intent);
            }
        });

        Button btnSendBroadcast = new Button(this);
        btnSendBroadcast.setText("发送广播给插件");
        btnSendBroadcast.setLayoutParams(buttonParams);
        linearLayout.addView(btnSendBroadcast);
        btnSendBroadcast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("com.jason.demoplugin.broadcast");
                sendBroadcast(intent);
            }
        });

        Button btnStartService1 = new Button(this);
        btnStartService1.setText("启动Service1");
        btnStartService1.setLayoutParams(buttonParams);
        linearLayout.addView(btnStartService1);
        btnStartService1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(new Intent().setComponent(
                        new ComponentName("com.jason.demoplugin", "com.jason.demoplugin.PluginService1")));
            }
        });

        final String PLUGIN_NAME = "plugin_name";
        final Uri PLUGIN_CONTENT_URI = Uri.parse("content://com.jason.demoplugin.provider/plugin");
        Button contentProviderInsert = new Button(this);
        contentProviderInsert.setText("ContentProvider插入");
        contentProviderInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                values.put(PLUGIN_NAME, "plugin com.jason.demoplugin " + new Date(System.currentTimeMillis()).toString());
                getContentResolver().insert(PLUGIN_CONTENT_URI, values);
            }
        });
        linearLayout.addView(contentProviderInsert);

        Button contentProviderQuery = new Button(this);
        contentProviderQuery.setText("ContentProvider查询");
        contentProviderQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = getContentResolver().query(PLUGIN_CONTENT_URI, null, null, null, null);
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        int index = cursor.getColumnIndex(PLUGIN_NAME);
                        if (index != -1) {
                            String pluginName = cursor.getString(index);
                            Log.d("TAG_HOOK", "PluginContentProvider -> query : pluginName = " + pluginName);
                            Toast.makeText(HookActivity.this, "ContentProvider 查询 pluginName = " + pluginName, Toast.LENGTH_SHORT);
                        }
                    }
                    cursor.close();
                }
            }
        });
        linearLayout.addView(contentProviderQuery);

        setContentView(linearLayout);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        MLog.d(MLog.TAG_HOOK, "HookActivity->" + "attachBaseContext ");
        super.attachBaseContext(newBase);

        InstrumentationHookHelper.hookActivityThreadInstrumentation();
        AmsHookHelper.hookActivityManagerNative();
        AmsHookHelper.hookActivityThreadHandlerCallback();
        PmsHookHelper.hookPMS(newBase);

        //提取插件apk（应该放在线程）
        PluginUtils.init(getApplicationContext());
        File pluginFile = PluginUtils.copyPluginApk(newBase, PLUGIN_APK_FILE_NAME);

        //加载插件中的类所使用的classLoader不同，CUSTOM_CLASS_LOADER方式需要使用custom的classloader才知道插件dex位置，PATCH_BASE_CLASS_LOADER方式则直接使用应用默认的classloader，因为插件dex已经并入宿主
        ClassLoader pluginClassloader = null;
        boolean needReplacePackageName = false;
        if (APK_LOAD_METHOD == CUSTOM_CLASS_LOADER) {
            LoadedApkClassLoaderHookHelper.hookLoadedApkInActivityThread(getFileStreamPath(PLUGIN_APK_FILE_NAME));
            pluginClassloader = LoadedApkClassLoaderHookHelper.getClassLoader(PLUGIN_PACKAGE_NAME);
            needReplacePackageName = false;
        } else if (APK_LOAD_METHOD == PATCH_BASE_CLASS_LOADER) {
            File dexFile = getFileStreamPath(PLUGIN_APK_FILE_NAME);
//            File optDexFile = getFileStreamPath(PLUGIN_APK_FILE_NAME);
            String optDexPath = PluginUtils.getPluginOptDexDir(PLUGIN_PACKAGE_NAME) + File.separator + "odex";
            File optDexFile = new File(optDexPath);
            BaseDexClassLoaderHookHelper.patchClassLoader(getClassLoader(), dexFile, optDexFile);
            pluginClassloader = newBase.getClassLoader();
            needReplacePackageName = true;
        }

        BroadcastLoadHelper.loadBroadcastReceiver(newBase, pluginFile,pluginClassloader);
        ServiceLoadHelper.loadService(newBase, pluginFile, needReplacePackageName);
        ContentProviderLoadHelper.installContentProvider(newBase, pluginFile,needReplacePackageName);
    }

}
