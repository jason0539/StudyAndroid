package com.jason.workdemo.page.progress;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;

/**
 * Created by liuzhenhui on 2016/10/27.
 */
public class ProgressDialogHandler extends Handler {

    public static final int SHOW_PROGRESS_DIALOG = 1;
    public static final int DISMISS_PROGRESS_DIALOG = 2;

    private ProgressDialog pd;
    private static Context mContext;

    private boolean cancelable;
    private ProgressCancelListener mProgressCancelListener;


    public static void init(Context context) {
        mContext = context;
    }

    public ProgressDialogHandler(ProgressCancelListener mProgressCancelListener,
                                 boolean cancelable) {
        super();
        this.mProgressCancelListener = mProgressCancelListener;
        this.cancelable = cancelable;
    }

    private void showProgressDialog() {
        if (pd == null && mContext != null) {
            pd = new ProgressDialog(mContext);
        }
        pd.setCancelable(cancelable);
        if (cancelable) {
            pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    mProgressCancelListener.onCancelProgress();
                }
            });
        }
        if (!pd.isShowing()) {
            pd.show();
        }
    }

    private void dismissProgressDialog() {
        if (pd != null) {
            pd.dismiss();
        }
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case SHOW_PROGRESS_DIALOG:
                showProgressDialog();
                break;
            case DISMISS_PROGRESS_DIALOG:
                dismissProgressDialog();
                break;
        }
    }

}