package com.jason.workdemo.demo.json;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.jason.common.utils.LayoutUtils;
import com.jason.common.utils.ScreenUtils;

/**
 * Created by liuzhenhui on 2017/9/4.
 */

public class JsonDemoActivity extends Activity {
    JsonUtils jsonUtils = new JsonUtils();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout linearLayout = LayoutUtils.getVerticalLinearLayout(this);
        ViewGroup.LayoutParams layoutParams = LayoutUtils.getLayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ScreenUtils.dpToPxInt(this, 50));

        Button serial = new Button(this);
        serial.setText("序列化");
        linearLayout.addView(serial, layoutParams);
        serial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jsonUtils.parseJson();
            }
        });
        setContentView(linearLayout);
    }
}
