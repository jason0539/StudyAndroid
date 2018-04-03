package com.jason.workdemo.demo.scroller;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

public class HelloScrollerView extends View {

    private Paint paint = new Paint();
    private Scroller scroller;
    private float lastX, lastY;
    private float currentX = 200;
    private float currentY = 200;
    private int textSize = 60;
    private GestureDetector gestureDetector;

    public HelloScrollerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        scroller = new Scroller(context);
        paint.setTextSize(textSize);
        GestureDetector.OnGestureListener onGestureListener = new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                int minX = 0;
                int maxX = (getWidth() - textSize * 2);
                int minY = textSize;
                int maxY = getHeight();
                scroller.fling((int) currentX, (int) currentY, (int) velocityX, (int) velocityY, minX, maxX, minY, maxY);
                return false;
            }
        };
        gestureDetector = new GestureDetector(getContext(), onGestureListener);
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawText("测试", currentX, currentY, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        super.onTouchEvent(ev);
        gestureDetector.onTouchEvent(ev);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!scroller.isFinished()) {
                    scroller.abortAnimation();
                }
                lastX = ev.getX();
                lastY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                currentX = (currentX + (ev.getX() - lastX));
                currentY = (currentY + (ev.getY() - lastY));
                lastX = ev.getX();
                lastY = ev.getY();
                break;
            case MotionEvent.ACTION_UP:
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