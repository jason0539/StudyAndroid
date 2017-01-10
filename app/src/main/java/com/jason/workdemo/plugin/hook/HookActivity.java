package com.jason.workdemo.plugin.hook;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.jason.common.utils.MLog;
import com.jason.common.utils.ScreenUtils;
import com.jason.workdemo.plugin.hook.ams.AmsHookHelper;
import com.jason.workdemo.plugin.hook.binder.BinderHookHelper;
import com.jason.workdemo.plugin.hook.instrumentation.InstrumentationHookHelper;
import com.jason.workdemo.plugin.hook.loadapk.BaseDexClassLoaderHookHelper;
import com.jason.workdemo.plugin.hook.loadapk.LoadedApkClassLoaderHookHelper;
import com.jason.workdemo.plugin.hook.pms.PmsHookHelper;

import java.io.File;

/**
 * Created by liuzhenhui on 2016/12/29.
 * http://weishu.me/2016/02/16/understand-plugin-framework-binder-hook/
 */
public class HookActivity extends Activity {
    public static final String TAG = HookActivity.class.getSimpleName();

    public static final String PLUGIN_APK_FILE_NAME = "app-debug.apk";
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
        btnLoadPluginApk.setText("加载插件apk");
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
        PluginUtils.copyPluginApk(newBase, PLUGIN_APK_FILE_NAME);
        if (APK_LOAD_METHOD == CUSTOM_CLASS_LOADER) {
            LoadedApkClassLoaderHookHelper.hookLoadedApkInActivityThread(getFileStreamPath(PLUGIN_APK_FILE_NAME));
        } else if (APK_LOAD_METHOD == PATCH_BASE_CLASS_LOADER) {
            File dexFile = getFileStreamPath(PLUGIN_APK_FILE_NAME);
//            File optDexFile = getFileStreamPath(PLUGIN_APK_FILE_NAME);
            String optDexPath = PluginUtils.getPluginOptDexDir(PLUGIN_PACKAGE_NAME) + File.separator + "odex";
            File optDexFile = new File(optDexPath);
            BaseDexClassLoaderHookHelper.patchClassLoader(getClassLoader(), dexFile, optDexFile);
        }
    }

}
