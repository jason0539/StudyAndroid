package com.jason.workdemo.demo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jason.common.utils.LayoutUtils;
import com.jason.common.utils.UtilStorage;

/**
 * Created by liuzhenhui on 2017/9/27.
 */

public class StorageActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout linearLayout = LayoutUtils.getVerticalLinearLayout(this);
        ViewGroup.LayoutParams layoutParams = LayoutUtils.getLayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        final TextView mTvDemo = new TextView(this);
        mTvDemo.setLayoutParams(layoutParams);
        mTvDemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTvDemo.setText(getStorageString());
            }
        });
        mTvDemo.setGravity(Gravity.CENTER);
        linearLayout.addView(mTvDemo);

        setContentView(linearLayout);
    }

    private String getStorageString() {
        boolean externalStorageAvailable = UtilStorage.isExternalStorageAvailable();
        String internalMemorySize = UtilStorage.getInternalMemorySize(this);
        String availableInternalMemorySize = UtilStorage.getAvailableInternalMemorySize(this);
        String externalMemorySize = UtilStorage.getExternalMemorySize(this);
        String availableExternalMemorySize = UtilStorage.getAvailableExternalMemorySize(this);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("externalStorageAvailable").append(externalStorageAvailable).append("\n");
        stringBuilder.append("internalMemorySize").append(internalMemorySize).append("\n");
        stringBuilder.append("availableInternalMemorySize").append(availableInternalMemorySize).append("\n");
        stringBuilder.append("externalMemorySize").append(externalMemorySize).append("\n");
        stringBuilder.append("availableExternalMemorySize").append(availableExternalMemorySize).append("\n");
        return stringBuilder.toString();
    }

}
