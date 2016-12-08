package com.jason.workdemo.demo.span;

import android.content.Context;
import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.jason.workdemo.util.MLog;

/**
 * Created by liuzhenhui on 2016/12/8.
 */
public class UserNickSpan extends ClickableSpan {
    private static final String TAG = UserNickSpan.class.getSimpleName();

    private Context context;

    private AtUser atUser;

    public UserNickSpan(Context context, AtUser atUser) {
        this.context = context;
        this.atUser = atUser;
    }

    @Override
    public void onClick(View widget) {
        MLog.d(MLog.TAG_SPAN, TAG + "->" + "onClick user.id = " + atUser.getUserId() + ",user.nick = " + atUser.getUserNick());
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setColor(Color.parseColor("#799bb8"));//set text color
        ds.setUnderlineText(false); // set to false to remove underline
    }
}
