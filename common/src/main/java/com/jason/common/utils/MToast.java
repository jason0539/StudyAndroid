package com.jason.common.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jason.common.R;

import java.util.List;

/**
 * Created by liuzhenhui on 2016/10/31.
 */
public class MToast {
    public static final long SHOW_NET_ERROR_TOAST_SPACE = 5000L;

    private static Context mContext;
    private static Toast toast;
    private static String mMsg = null;
    private static int DEFAULT_POSITION = 0;

    public static final void init(Context context) {
        mContext = context;
    }

    public static void show(String message) {
        if (mContext != null) {
            show(R.drawable.icon_warning, message, Gravity.CENTER);
        }
    }

    public static void showWarning(String message) {
        if (mContext != null) {
            show(R.drawable.icon_warning, message, Gravity.CENTER);
        }
    }

    public static void showSuccess(String message) {
        if (mContext != null) {
            show(R.drawable.icon_success, message, Gravity.CENTER);
        }
    }

    private static void show(final int iconId, final String message, final int gravity) {
        if (mContext == null || TextUtils.isEmpty(message)) {
            return;
        }
        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> infos = am.getRunningTasks(1);
        if (infos != null && infos.get(0) != null && mContext.getPackageName().equals(infos.get(0).baseActivity.getPackageName())) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    if (toast == null) {
                        toast = new Toast(mContext);
                        View view = LayoutInflater.from(mContext).inflate(R.layout.view_toast, null);
                        toast.setView(view);
                        if (gravity != DEFAULT_POSITION) {
                            toast.setGravity(gravity, 0, 0);
                        }
                    }
                    int duration = Toast.LENGTH_SHORT;
                    if (message.length() > 15) {
                        duration = Toast.LENGTH_LONG;
                    }
                    if (!toast.getView().isShown()) {
                        ((ImageView) toast.getView().findViewById(R.id.iv_toast_icon)).setImageResource(iconId);
                        ((TextView) toast.getView().findViewById(R.id.tv_toast_message)).setText(message);
                        toast.setDuration(duration);
                        toast.show();
                    }
                }
            });

        }
    }

}