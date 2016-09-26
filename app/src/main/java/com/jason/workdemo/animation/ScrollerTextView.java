package com.jason.workdemo.animation;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Scroller;
import android.widget.TextView;

/**
 * liuzhenhui 16/6/4.下午11:10
 */
public class ScrollerTextView extends TextView {

    private Scroller mScroller;

    public ScrollerTextView(Context context) {
        super(context);
        init();
    }

    public ScrollerTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ScrollerTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mScroller = new Scroller(getContext());
    }

    /**
     * @param destX
     * @param destY 将内容向下移动的距离(px)
     */
    public void smoothScrollBy(int destX, int destY) {
        // destY为正数则content向上移动
        mScroller.startScroll(0, getScrollY(), 0, -destY, 1000);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }
}
