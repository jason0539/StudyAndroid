package com.jason.common.view.IndexableListView;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.jason.common.utils.MLog;


/**
 * Created by liuzhenhui on 2016/11/25.
 */
public class IndexableListView extends ListView {
    private static final String TAG = IndexableListView.class.getSimpleName();

    private boolean mIsFastScrollEnabled = false;
    private IndexScroller mScroller = null;
    private GestureDetector mGestureDetector = null;

    public IndexableListView(Context context) {
        super(context);
    }

    public IndexableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IndexableListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean isFastScrollEnabled() {
        return mIsFastScrollEnabled;
    }

    @Override
    public void setFastScrollEnabled(boolean enabled) {
        mIsFastScrollEnabled = enabled;
        if (mIsFastScrollEnabled) {
            if (mScroller == null) {
                mScroller = new IndexScroller(getContext(), this);
            }
        } else {
            if (mScroller != null) {
                mScroller.hide();
                mScroller = null;
            }
        }
    }

    public void setNeedBackground(boolean needBackground) {
        if (mScroller != null) {
            mScroller.setNeedBackground(needBackground);
            postInvalidate();
        }
    }

    public void setAutoShowAndHide(boolean autoShowAndHide) {
        if (mScroller != null) {
            mScroller.setAutoShowAndHide(autoShowAndHide);
            postInvalidate();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        // Overlay index bar
        if (mScroller != null) {
            mScroller.draw(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // Intercept ListView's touch event
        if (mScroller != null && mScroller.onTouchEvent(ev)) {
            MLog.d(MLog.TAG_INDEXABLELV, TAG + "->" + "onTouchEvent mScroller handled");
            return true;
        }

        if (mGestureDetector == null) {
            mGestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2,
                                       float velocityX, float velocityY) {
                    MLog.d(MLog.TAG_INDEXABLELV, TAG + "->" + "onFling ");
                    if (mScroller != null) {
                        mScroller.show();
                    }
                    return super.onFling(e1, e2, velocityX, velocityY);
                }

            });
        }
        mGestureDetector.onTouchEvent(ev);

        return super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mScroller != null && mScroller.contains(ev.getX(), ev.getY())) {
            MLog.d(MLog.TAG_INDEXABLELV, TAG + "->" + "onInterceptTouchEvent intercept");
            return true;
        } else {
            MLog.d(MLog.TAG_INDEXABLELV, TAG + "->" + "onInterceptTouchEvent not intercept");
        }

        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
        if (mScroller != null) {
            mScroller.setAdapter(adapter);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mScroller != null) {
            mScroller.onSizeChanged(w, h, oldw, oldh);
        }
    }

}