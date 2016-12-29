package com.jason.workdemo.plugin.hook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.jason.common.utils.MLog;

/**
 * Created by liuzhenhui on 2016/12/29.
 */
public class HookActivity extends Activity {
    public static final String TAG = HookActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MLog.d(MLog.TAG_HOOK,"HookActivity->"+"onCreate ");
        super.onCreate(savedInstanceState);
        Button tv = new Button(this);
        tv.setText("测试界面");
        setContentView(tv);

        try {
            HookHelper.hookActivityInstrumentation(this);
        } catch (Exception e) {
            MLog.d(MLog.TAG_HOOK,"HookActivity->"+"onCreate hook activity error " + e.toString());
            e.printStackTrace();
        }

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
//                getApplicationContext().startActivity(intent);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        MLog.d(MLog.TAG_HOOK,"HookActivity->"+"attachBaseContext ");
        super.attachBaseContext(newBase);
        try {
            HookHelper.attachContext();
        } catch (Exception e) {
            MLog.d(MLog.TAG_HOOK, "HookActivity->" + "attachBaseContext exception = " + e.toString());
        }
    }

}
