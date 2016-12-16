package com.jason.workdemo.touchevent.event;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.jason.common.utils.MLog;
import com.jason.workdemo.R;

/**
 * liuzhenhui 16/6/4.下午4:29
 */
public class TouchActivity extends Activity {

    private TouchEventLayout mTouchEventLayout;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch);

        mTouchEventLayout = (TouchEventLayout) findViewById(R.id.layout_touch);
        mTouchEventLayout.setOnTouchListener(mLayoutTouchListener);

        mTextView = (TextView) findViewById(R.id.tv_show);
        mTextView.setOnTouchListener(mTextViewTouchListener);
    }

    View.OnTouchListener mLayoutTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int action = event.getAction();
            boolean consumed = true;
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    MLog.d("lzh", "TouchEventLayout.OnTouchListener-->ACTION_DOWN");
                    consumed = false;
                    break;
                case MotionEvent.ACTION_MOVE:
                    MLog.d("lzh", "TouchEventLayout.OnTouchListener-->ACTION_MOVE");
                    consumed = false;
                    break;
                case MotionEvent.ACTION_UP:
                    MLog.d("lzh", "TouchEventLayout.OnTouchListener-->ACTION_UP");
                    consumed = false;
                    break;
            }
            return consumed;
        }
    };

    View.OnTouchListener mTextViewTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    MLog.d("lzh", "TextView.OnTouchListener-->ACTION_DOWN");
                    break;
                case MotionEvent.ACTION_MOVE:
                    MLog.d("lzh", "TextView.OnTouchListener-->ACTION_MOVE");
                    break;
                case MotionEvent.ACTION_UP:
                    MLog.d("lzh", "TextView.OnTouchListener-->ACTION_UP");
                    break;
            }
            return false;
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                MLog.d("lzh", "TouchActivity.onTouchEvent-->ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                MLog.d("lzh", "TouchActivity.onTouchEvent-->ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                MLog.d("lzh", "TouchActivity.onTouchEvent-->ACTION_UP");
                break;
        }
        return false;
    }
}
