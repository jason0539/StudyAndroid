package com.jason.workdemo.touchevent.gesture;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.jason.common.utils.MLog;
import com.jason.workdemo.R;

/**
 * liuzhenhui 16/6/4.下午4:33
 */
public class GestureActivity extends Activity {

    private TextView mTextView;
    private GestureDetector mGestureDecector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture);

        mGestureDecector = new GestureDetector(this, mGestureLis);
        // 防止长按屏幕后无法拖动
        mGestureDecector.setIsLongpressEnabled(false);

        mTextView = (TextView) findViewById(R.id.tv_show);
        mTextView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mGestureDecector.onTouchEvent(event);
            }
        });
    }

    GestureDetector.OnGestureListener mGestureLis = new GestureDetector.OnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            MLog.d("lzh", "GestureActivity-->onDown");
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {
            MLog.d("lzh", "GestureActivity-->onShowPress");
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            MLog.d("lzh", "GestureActivity-->onSingleTapUp");
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            MLog.d("lzh", "GestureActivity-->onScroll : distanceX = " + distanceX + ",distanceY = " + distanceY + "\n" +
                    "e1.getRawY = " + e1.getRawY() + ", e2.getRawY = " + e2.getRawY());
            //            mTextView.scrollTo((int) (e1.getRawX() - e2.getRawX()), (int) (e1.getRawY() - e2.getRawY()));
            mTextView.scrollBy((int) distanceX, (int) distanceY);
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            MLog.d("lzh", "GestureActivity-->onLongPress");
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            MLog.d("lzh", "GestureActivity-->onFling");
            return false;
        }
    };

}
