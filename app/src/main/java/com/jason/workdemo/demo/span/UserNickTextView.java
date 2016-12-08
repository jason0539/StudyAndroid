package com.jason.workdemo.demo.span;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.widget.TextView;

import com.jason.workdemo.util.MLog;

import java.util.ArrayList;

/**
 * Created by liuzhenhui on 2016/12/8.
 */
public class UserNickTextView extends TextView {
    public static final String TAG = UserNickTextView.class.getSimpleName();

    public UserNickTextView(Context context) {
        super(context);
    }

    public UserNickTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UserNickTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setText(CharSequence charSequence, BufferType type) {
        String text = charSequence.toString();
        MLog.d(MLog.TAG_SPAN, TAG + "->" + "setText : " + text);
        ArrayList<AtUser> atUsers = AtUserUtil.getAtUsers(text);
        if (atUsers != null && atUsers.size() > 0) {
            String content = AtUserUtil.replaceAtUserString(text, atUsers);
            MLog.d(MLog.TAG_SPAN, TAG + "->" + "setText content = " + content);

            SpannableString ss = new SpannableString(content);
            for (int i = atUsers.size(); i > 0; i--) {
                AtUser atUser = atUsers.get(i - 1);
                String at = TextUtils.concat("@", atUser.userNick).toString();
                int mIndex = content.indexOf(at);
                int last = 0;
                String temp = content;
                while (mIndex >= 0) {
                    ss.setSpan(new UserNickSpan(getContext(), atUser), last + mIndex, last + mIndex + at.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    temp = temp.substring(mIndex + at.length());
                    last += mIndex + at.length();
                    mIndex = temp.indexOf(at);
                }
            }
            setMovementMethod(LinkMovementMethod.getInstance());
            setHighlightColor(Color.TRANSPARENT);
            super.setText(ss, type);
        } else {
            super.setText(text, type);
        }
    }
}
