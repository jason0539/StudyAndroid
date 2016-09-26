package com.jason.workdemo.animation;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import com.jason.workdemo.R;

/**
 * liuzhenhui 16/6/4.下午8:18
 * <p/>
 * 动画实现方式：
 * 1、scrollTo、scrollBy（参考GestureActivity）
 * 2、常规View动画、Property属性动画
 * 3、改变params
 */
public class AnimActivity extends Activity implements View.OnClickListener {

    private Button mBtnViewAnim;
    private Button mBtnPropertyAnim;
    private Button mBtnParamsAnim;
    private View mViewTouch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);
        mViewTouch = findViewById(R.id.layout_touch);
        mBtnViewAnim = (Button) findViewById(R.id.btn_anim_view);
        mBtnPropertyAnim = (Button) findViewById(R.id.btn_anim_property);
        mBtnParamsAnim = (Button) findViewById(R.id.btn_anim_params);

        mBtnViewAnim.setOnClickListener(this);
        mBtnPropertyAnim.setOnClickListener(this);
        mBtnParamsAnim.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_anim_view:
                startViewAnim();
                break;
            case R.id.btn_anim_property:
                startPropertyAnim();
                break;
            case R.id.btn_anim_params:
                startParamsAnim();
                break;
        }
    }

    private void startViewAnim() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.translate_anim);
        mBtnViewAnim.startAnimation(animation);
    }

    private void startPropertyAnim() {
        PropertyValuesHolder translateX = PropertyValuesHolder.ofFloat("translationX", 0, 500);
        PropertyValuesHolder translateY = PropertyValuesHolder.ofFloat("translationY", 0, 500);

        ObjectAnimator.ofPropertyValuesHolder(mBtnPropertyAnim, translateX, translateY).setDuration(500).start();
        //        ObjectAnimator.ofFloat(mBtnPropertyAnim, "translationX", 0, 500).setDuration(500).start();
    }

    private void startParamsAnim() {
        final GestureDetector gestureDetector = new GestureDetector(this, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return true;
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
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mBtnParamsAnim.getLayoutParams();
                params.leftMargin -= distanceX;
                params.bottomMargin += distanceY;
                //                或者
                //                mBtnParamsAnim.requestLayout();
                mBtnParamsAnim.setLayoutParams(params);
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return false;
            }
        });
        gestureDetector.setIsLongpressEnabled(false);
        mViewTouch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });
    }
}
