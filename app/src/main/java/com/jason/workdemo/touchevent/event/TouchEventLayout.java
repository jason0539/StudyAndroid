package com.jason.workdemo.touchevent.event;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.jason.common.utils.MLog;

/**
 * liuzhenhui 16/6/4.上午10:53
 */
public class TouchEventLayout extends LinearLayout {
    public TouchEventLayout(Context context) {
        super(context);
    }

    public TouchEventLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchEventLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //        int action = ev.getAction();
        //        switch (action) {
        //            case MotionEvent.ACTION_DOWN:
        //                MLog.d("lzh", "TouchEventLayout.dispatchTouchEvent-->ACTION_DOWN");
        //                break;
        //            case MotionEvent.ACTION_MOVE:
        //                MLog.d("lzh", "TouchEventLayout.dispatchTouchEvent-->ACTION_MOVE");
        //                break;
        //            case MotionEvent.ACTION_UP:
        //                MLog.d("lzh", "TouchEventLayout.dispatchTouchEvent-->ACTION_UP");
        //                break;
        //        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        boolean consumed = true;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                MLog.d("lzh", "TouchEventLayout.onInterceptTouchEvent-->ACTION_DOWN");
                consumed = true;
                break;
            case MotionEvent.ACTION_MOVE:
                MLog.d("lzh", "TouchEventLayout.onInterceptTouchEvent-->ACTION_MOVE");
                consumed = true;
                break;
            case MotionEvent.ACTION_UP:
                MLog.d("lzh", "TouchEventLayout.onInterceptTouchEvent-->ACTION_UP");
                consumed = true;
                break;
        }
        return consumed;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        boolean consumed = true;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                MLog.d("lzh", "TouchEventLayout.onTouchEvent-->ACTION_DOWN");
                consumed = true;
                break;
            case MotionEvent.ACTION_MOVE:
                MLog.d("lzh", "TouchEventLayout.onTouchEvent-->ACTION_MOVE");
                consumed = true;
                break;
            case MotionEvent.ACTION_UP:
                MLog.d("lzh", "TouchEventLayout.onTouchEvent-->ACTION_UP");
                consumed = true;
                break;
        }
        return consumed;
    }

}
