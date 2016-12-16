package com.jason.workdemo.page;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;

import com.jason.common.utils.MLog;
import com.jason.common.utils.MToast;
import com.jason.common.utils.ResUtils;
import com.jason.workdemo.MainActivity;
import com.jason.workdemo.R;
import com.jason.workdemo.page.fragment.BaseFragment;
import com.jason.workdemo.page.fragment.ContentFragment;
import com.jason.workdemo.page.fragment.JLFragmentManager;
import com.jason.workdemo.page.progress.ProgressDialogHandler;

/**
 * Created by liuzhenhui on 2016/12/16.
 */
public class FragmentMainActivity extends FragmentActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private JLFragmentManager mJlFragmentManager;
    private View mForbidTouchView;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_main);
        mForbidTouchView = findViewById(R.id.view_main_forbid_touch);

        mJlFragmentManager = new JLFragmentManager(this);
        BaseFragment.initBeforeAll(this, mJlFragmentManager);
        MToast.init(getApplicationContext());
        ResUtils.init(getApplicationContext());
        ProgressDialogHandler.init(this);

        mJlFragmentManager.showFragment(JLFragmentManager.TYPE_SPLASH);
    }

    public void forbidTouch(boolean forbid) {
        if (forbid) {
            mForbidTouchView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
            mForbidTouchView.setVisibility(View.VISIBLE);
        } else {
            mForbidTouchView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        ContentFragment fragment = mJlFragmentManager.getCurrentFragment();
        if (fragment != null && fragment.onBackPressed())
            return;
        if (mJlFragmentManager.getFragmentStackSize() > 0)
            mJlFragmentManager.back(null);
    }

    public void exitApp() {
        finish();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        MLog.d(MLog.TAG_LOGIN, TAG + "->" + "onActivityResult ");
        if (mJlFragmentManager.getCurrentFragment() != null) {
            mJlFragmentManager.getCurrentFragment().onActivityResult(requestCode, resultCode, data);
        }
    }
}