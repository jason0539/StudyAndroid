package com.jason.workdemo.demo.scroller;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.Scroller;

import com.jason.common.utils.MLog;

public class HelloScrollerView extends View {

    private Paint paint = new Paint();
    private VelocityTracker velocityTracker;
    private Scroller scroller;
    private float lastX;
    private float currentX = 200;
    private float currentY = 200;
    private int textSize = 60;

    public HelloScrollerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        scroller = new Scroller(context);
        paint.setTextSize(textSize);
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawText("测试", currentX, currentY, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        super.onTouchEvent(ev);

        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain();
        }
        velocityTracker.addMovement(ev);

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!scroller.isFinished()) {
                    scroller.abortAnimation();
                }
                lastX = ev.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                currentX = (currentX + (ev.getX() - lastX));
                lastX = ev.getX();
                break;
            case MotionEvent.ACTION_UP:
                velocityTracker.computeCurrentVelocity(1000);
                MLog.d(MLog.TAG_SCROLL, "HelloScrollerView->onTouchEvent vx = " + (int) velocityTracker.getXVelocity());
                scroller.fling((int) currentX, (int) currentY, (int) velocityTracker.getXVelocity(), (int) velocityTracker.getYVelocity(), 0, (getWidth() - textSize * 2), textSize, getHeight());
                velocityTracker.recycle();
                velocityTracker = null;
                break;
        }
        invalidate();
        return true;
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            currentX = scroller.getCurrX();
            currentY = scroller.getCurrY();
            invalidate();
        }
    }
}