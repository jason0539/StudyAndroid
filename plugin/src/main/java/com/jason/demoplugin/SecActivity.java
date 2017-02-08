package com.jason.demoplugin;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Created by liuzhenhui on 2017/1/10.
 */
public class SecActivity extends AppCompatActivity {
    public static final String TAG = SecActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("SecActivity");

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setGravity(Gravity.CENTER_VERTICAL);

        Button sendBroadcast = new Button(this);
        sendBroadcast.setBackgroundColor(Color.parseColor("#FACC2E"));
        sendBroadcast.setText("点击发送广播");
        sendBroadcast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("com.jason.demoplugin.broadcast");
                sendBroadcast(intent);
            }
        });
        linearLayout.addView(sendBroadcast);

        setContentView(linearLayout);
    }
}
