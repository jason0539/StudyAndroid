package com.jason.workdemo.demo.span;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.jason.workdemo.R;

/**
 * Created by liuzhenhui on 2016/12/8.
 */
public class SpanDemoActivity extends Activity {
    public static final String TAG = SpanDemoActivity.class.getSimpleName();

    TextView mTvSpan;
    UserNickTextView mTvNick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_span);
        mTvSpan = (TextView) findViewById(R.id.tv_span_demo);
        mTvNick = (UserNickTextView) findViewById(R.id.tv_span_nick);

        String strUserEnter = "您的好友@{uid:80000,nick:一个小李}加入了交流@{uid:80000,nick:呼唤TA}";
        mTvNick.setText(strUserEnter);
//
//        ArrayList<AtUser> atUsers = AtUserUtil.getAtUsers(strUserEnter);
//        for (AtUser atUser : atUsers) {
//            MLog.d(MLog.TAG_SPAN, TAG + "->" + "onCreate " + atUser.getUserId() + "," + atUser.getUserNick());
//        }
//
//        String content = AtUserUtil.replaceAtUserString(strUserEnter,atUsers);
//        MLog.d(MLog.TAG_SPAN,TAG+"->"+"onCreate content = " + content);
//
//        SpannableString ss = new SpannableString(content);
//        if (atUsers != null && atUsers.size() >0) {
//            for (int i = atUsers.size(); i > 0; i--) {
//                AtUser atUser = atUsers.get(i - 1);
//                String at = TextUtils.concat("@", atUser.userNick).toString();
//                int mIndex = content.indexOf(at);
//                int last = 0;
//                String temp = content;
//                while (mIndex >= 0) {
//                    ss.setSpan(new UserNickSpan(this, atUser), last + mIndex, last + mIndex + at.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    temp = temp.substring(mIndex + at.length());
//                    last += mIndex + at.length();
//                    mIndex = temp.indexOf(at);
//                }
//            }
//        }
//        mTvSpan.setMovementMethod(LinkMovementMethod.getInstance());
//        mTvSpan.setHighlightColor(Color.TRANSPARENT);
//        mTvSpan.setText(ss);
    }
}
