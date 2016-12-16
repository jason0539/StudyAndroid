package com.jason.workdemo.demo.scroller;

import android.content.Context;
import android.support.v4.view.ViewConfigurationCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

import com.jason.common.utils.MLog;

/**
 * Created by liuzhenhui on 2016/12/8.
 * http://blog.csdn.net/sinyu890807/article/details/48719871
 */
public class ScrollerLayout extends ViewGroup {
    public static final String TAG = ScrollerLayout.class.getSimpleName();


    Scroller mScroller;//用于完成滚动操作的实例
    int mTouchSlop;//判定为拖动的最小移动像素数
    float mXDown;//手指按下时x坐标
    float mXMove;//手指移动实时x坐标
    float mXLastMove;//上次触发ACTION_MOVE事件时的屏幕坐标
    int leftBorder;//界面可滚动的左边界
    int rightBorder;//界面可滚动的右边界

    public ScrollerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        //第一步，创建Scroller实例
        mScroller = new Scroller(context);
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        //获取TouchSlop值
        mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(viewConfiguration);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            //为ScrollerLayout中的每个子控件测量大小
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed) {
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childView = getChildAt(i);
                int left = i * childView.getMeasuredWidth();
                int right = (i + 1) * childView.getMeasuredWidth();
                // 为ScrollerLayout中的每一个子控件在水平方向上进行布局
                childView.layout(left, 0, right, childView.getMeasuredHeight());
            }
            leftBorder = getChildAt(0).getLeft();
            rightBorder = getChildAt(childCount - 1).getRight();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mXDown = ev.getRawX();
                MLog.d(MLog.TAG_SCROLL, TAG + "->" + "onInterceptTouchEvent ACTION_DOWN mXDown = " + mXDown);
                mXLastMove = mXDown;
                break;
            case MotionEvent.ACTION_MOVE:
                mXMove = ev.getRawX();
                MLog.d(MLog.TAG_SCROLL, TAG + "->" + "onInterceptTouchEvent ACTION_MOVE mXMove = " + mXMove);
                float diff = Math.abs(mXMove - mXDown);
                mXLastMove = mXMove;
                if (diff > mTouchSlop) {
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                mXMove = event.getRawX();
                MLog.d(MLog.TAG_SCROLL, TAG + "->" + "onTouchEvent ACTION_MOVE mXMove = " + mXMove + ", getScrollX = " + getScrollX());
                int scrolledX = (int) (mXLastMove - mXMove);
                if (getScrollX() + scrolledX < leftBorder) {
                    MLog.d(MLog.TAG_SCROLL, TAG + "->" + "onTouchEvent ACTION_MOVE 到达左边界");
                    scrollTo(leftBorder, 0);
                    return true;
                } else if (getScrollX() + getWidth() + scrolledX > rightBorder) {
                    scrollTo(rightBorder - getWidth(), 0);
                    return true;
                }
                scrollBy(scrolledX, 0);
                mXLastMove = mXMove;
                break;
            case MotionEvent.ACTION_UP:
                // 当手指抬起时，根据当前的滚动值来判定应该滚动到哪个子控件的界面
                int targetIndex = (getScrollX() + getWidth() / 2) / getWidth();
                int dx = targetIndex * getWidth() - getScrollX();
                //第二步，调用startScroll方法来初始化滚动数据并刷新界面
                mScroller.startScroll(getScrollX(), 0, dx, 0);
                invalidate();
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
    }
}
