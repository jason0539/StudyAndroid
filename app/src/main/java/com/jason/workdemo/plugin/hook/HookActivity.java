package com.jason.workdemo.plugin.hook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.jason.common.utils.MLog;
import com.jason.common.utils.ScreenUtils;

/**
 * Created by liuzhenhui on 2016/12/29.
 */
public class HookActivity extends Activity {
    public static final String TAG = HookActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MLog.d(MLog.TAG_HOOK, "HookActivity->" + "onCreate ");
        super.onCreate(savedInstanceState);

        //Hook Activity的instrumentation
        try {
            HookHelper.hookActivityInstrumentation(this);
        } catch (Exception e) {
            MLog.d(MLog.TAG_HOOK, "HookActivity->" + "onCreate hook activity error " + e.toString());
            e.printStackTrace();
        }

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ScreenUtils.dpToPxInt(this, 50));
        Button tv = new Button(this);
        tv.setLayoutParams(buttonParams);
        tv.setText("HookActivity");
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
//                startActivity(intent);
            }
        });

        linearLayout.addView(tv);

        setContentView(linearLayout);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        MLog.d(MLog.TAG_HOOK, "HookActivity->" + "attachBaseContext ");
        super.attachBaseContext(newBase);
        try {
            HookHelper.hookActivityThreadInstrumentation();
        } catch (Exception e) {
            MLog.d(MLog.TAG_HOOK, "HookActivity->" + "attachBaseContext exception = " + e.toString());
        }
    }

}
