package com.jason.demoplugin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button tvJump;
    Button tvBroadcast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("MainActivity");

        setContentView(R.layout.activity_main);
        tvJump = (Button) findViewById(R.id.tv_jump);
        tvJump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SecActivity.class);
                startActivity(intent);
            }
        });

        tvBroadcast = (Button) findViewById(R.id.tv_broadcast);
        tvBroadcast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("com.jason.demoplugin.broadcast");
                sendBroadcast(intent);
            }
        });
    }
}
