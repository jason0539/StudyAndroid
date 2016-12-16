package com.jason.common.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by liuzhenhui on 2016/11/23.
 */
public class CountButton extends Button implements View.OnClickListener {


    /*
     倒计时时长,默认计时时间
     */
    private long defaultTime = 60 * 1000;
    private long time = defaultTime;

    /*
    开始执行计时的类,可以每间隔一秒执行任务
     */
    private Timer timer;

    /*
    执行的任务
     */
    private TimerTask task;

    /*
    默认文案
     */
    private String defaultText = "Send";
    /*
    计时完成之后显示的文案
     */
    private String finishText = "Send";


    /*
    正常的点击事件监听
     */
    private OnClickListener onClickListener;

    /**
     * 点击事件监听，事件处理中返回true or false，控制计时是否开始
     */
    private CountButtonClickListener countButtonClickListener;

    public CountButton(Context context) {
        super(context);
        initView();
    }

    public CountButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public CountButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }


    private void initView() {
        if (!TextUtils.isEmpty(getText())) {
            defaultText = getText().toString().trim();
        }
        this.setText(defaultText);
        setOnClickListener(this);
    }

    /*
    更新显示文案
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            CountButton.this.setText(time / 1000 + "");
            time -= 1000;
            if (time < 0) {
                CountButton.this.setEnabled(true);
                CountButton.this.setText(finishText);
                clearTimer();
                time = defaultTime;
            }
        }
    };

    /*
    清除倒计时
     */
    private void clearTimer() {
        if (task != null) {
            task.cancel();
            task = null;
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    /*
    初始化时间
     */
    private void initTimer() {
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(1);
            }
        };
    }

    @Override
    protected void onDetachedFromWindow() {
        clearTimer();
        super.onDetachedFromWindow();
    }


    public void setDefaultText(String defaultText) {
        this.defaultText = defaultText;
    }


    public void setFinishText(String finishText) {
        this.finishText = finishText;
    }


    public void setDefaultTime(long defaultTime) {
        this.defaultTime = defaultTime;
    }

    @Override
    public void onClick(View view) {
        boolean countStart = false;
        //外部有倒计时开始条件的话，则只有外部允许倒计时才开始倒计时，否则默认点击直接倒计时
        if (countButtonClickListener != null) {
            countStart = countButtonClickListener.onClick();
        } else {
            countStart = true;
        }
        if (onClickListener != null) {
            onClickListener.onClick(view);
        }
        if (countStart) {
            start();
        }
    }

    public void start() {
        initTimer();
        this.setText(time / 1000 + "");
        this.setEnabled(false);
        timer.schedule(task, 0, 1000);
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        if (l instanceof CountButton) {
            super.setOnClickListener(l);
        } else {
            this.onClickListener = l;
        }
    }

    public void setCountButtonOnclickListenr(CountButtonClickListener lis) {
        countButtonClickListener = lis;
    }

    public interface CountButtonClickListener {
        boolean onClick();
    }
}