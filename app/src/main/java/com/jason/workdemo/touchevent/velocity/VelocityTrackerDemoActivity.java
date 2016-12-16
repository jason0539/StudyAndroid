package com.jason.workdemo.touchevent.velocity;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.TextView;

import com.jason.common.utils.MLog;
import com.jason.workdemo.R;

/**
 * liuzhenhui 16/6/4.上午10:01
 */
public class VelocityTrackerDemoActivity extends Activity {

    private TextView mTextView;

    private VelocityTracker mVelocityTracker;
    private int mMaxVelocity;
    private int mPointerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch);

        mTextView = (TextView) findViewById(R.id.tv_show);
        mTextView.setOnTouchListener(mTextViewTouchListener);

        mMaxVelocity = ViewConfiguration.getMaximumFlingVelocity();
    }

    View.OnTouchListener mTextViewTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int action = event.getAction();
            calculateMotionVelocity(event);
            final VelocityTracker velocityTracker = mVelocityTracker;
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    MLog.d("lzh", "mTextViewTouchListener-->ACTION_DOWN");
                    //求第一个触点的id， 此时可能有多个触点，但至少一个
                    mPointerId = event.getPointerId(0);
                    break;
                case MotionEvent.ACTION_MOVE:
                    velocityTracker.computeCurrentVelocity(1000, mMaxVelocity);
                    float velocityX = velocityTracker.getXVelocity(mPointerId);
                    float velocityY = velocityTracker.getYVelocity(mPointerId);
                    handleVelocity(velocityX, velocityY);
                    break;
                case MotionEvent.ACTION_UP:
                    MLog.d("lzh", "mTextViewTouchListener-->ACTION_UP");
                    releaseVelocityTracker();
                    break;
                case MotionEvent.ACTION_CANCEL:
                    releaseVelocityTracker();
                    break;
            }
            return false;
        }
    };

    private void releaseVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.clear();
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    private static final String sFormatStr = "velocityX=%f\nvelocityY=%f";

    private void handleVelocity(float velocityX, float velocityY) {
        final String velocityInfo = String.format(sFormatStr, velocityX, velocityY);
        mTextView.setText(velocityInfo);
    }

    private void calculateMotionVelocity(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

}
