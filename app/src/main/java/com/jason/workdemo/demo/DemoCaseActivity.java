package com.jason.workdemo.demo;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.jason.common.utils.LayoutUtils;
import com.jason.common.utils.MLog;
import com.jason.common.utils.ScreenUtils;
import com.jason.common.utils.UtilDate;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by liuzhenhui on 2017/12/29.
 */

public class DemoCaseActivity extends Activity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout linearLayout = LayoutUtils.getVerticalLinearLayout(this);
        ViewGroup.LayoutParams layoutParams = LayoutUtils.getLayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ScreenUtils.dpToPxInt(this, 50));

        Button clip = new Button(this);
        clip.setText("粘贴板");
        clip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String format1 = UtilDate.formatDate(new Date());
                ClipboardManager clipboardManager = (ClipboardManager) DemoCaseActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
                clipboardManager.setText("日期"+format1);
            }
        });
        linearLayout.addView(clip, layoutParams);

        Button randomChoose = new Button(this);
        randomChoose.setText("时间");
        randomChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String random = String.valueOf(System.currentTimeMillis());
                int num = Integer.valueOf(random.substring(random.length()-1));
                MLog.d(MLog.TAG_JSON,"JsonDemoActivity->onClick " + num + ":choosed = " + (num > 5));

                long timeNow = System.currentTimeMillis();
                Date date = new Date();
                date.setYear(118);
                date.setMonth(3);
                date.setDate(1);
                long timeEnd = date.getTime();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                String dateEnd = format.format(timeEnd);
                String dateNow = format.format(timeNow);
                long diff = timeEnd - timeNow;
                MLog.d(MLog.TAG_JSON,"DemoCaseActivity->onClick 现在:"+dateNow + ",结束："+dateEnd+",diff="+diff);
                if (timeNow < timeEnd) {
                    MLog.d(MLog.TAG_JSON,"DemoCaseActivity->onClick 还未结束");
                }else {
                    MLog.d(MLog.TAG_JSON,"DemoCaseActivity->onClick 结束了");
                }
            }
        });
        linearLayout.addView(randomChoose,layoutParams);

        setContentView(linearLayout);
    }
}
