package com.jason.workdemo.animation;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.jason.workdemo.R;

/**
 * liuzhenhui 16/6/4.下午10:54
 */
public class ScrollActivity extends Activity implements OnClickListener {

    private ScrollerTextView mTvScroller;
    private TextView mTvAnim;
    private TextView mTvDelay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll);
        mTvScroller = (ScrollerTextView) findViewById(R.id.btn_scroll_scroller);
        mTvAnim = (TextView) findViewById(R.id.btn_scroll_anim);
        mTvDelay = (TextView) findViewById(R.id.btn_scroll_delay);

        mTvScroller.setOnClickListener(this);
        mTvAnim.setOnClickListener(this);
        mTvDelay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_scroll_scroller:
                startScrollerScroll();
                break;
            case R.id.btn_scroll_anim:
                startAnimScroll();
                break;
            case R.id.btn_scroll_delay:
                startDelayScroll(500);
                break;
        }
    }

    private void startScrollerScroll() {
        mTvScroller.smoothScrollBy(0, 100);
    }

    private void startAnimScroll() {
        final int startY = 0;
        final int deltaY = -500;
        final ValueAnimator animator = ValueAnimator.ofInt(0, 1).setDuration(1000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = animator.getAnimatedFraction();
                // 负值则内容向下移动
                mTvAnim.scrollTo(0, startY + (int) (deltaY * fraction));
            }
        });
        animator.start();
    }

    final int MSG_SCROLL_TO = 1;
    final int FRAME_COUNT = 60;
    final int DELAYD_TIME = 15;
    int mCount = 0;

    private void startDelayScroll(final int destY) {
        final Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MSG_SCROLL_TO:
                        mCount++;
                        if (mCount <= FRAME_COUNT) {
                            float fraction = mCount / (float) FRAME_COUNT;
                            int scollY = (int) (fraction * destY);
                            mTvDelay.scrollTo(0, -scollY);
                            sendEmptyMessageDelayed(MSG_SCROLL_TO, DELAYD_TIME);
                        } else {
                            mCount = 0;
                        }
                        break;
                }
            }
        };
        mHandler.sendEmptyMessageDelayed(MSG_SCROLL_TO, DELAYD_TIME);
    }
}
