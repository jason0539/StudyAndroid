package com.jason.demoplugin;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

//继承AppCompat的方式导致classnotfound
//public class MainActivity extends AppCompatActivity {
public class MainActivity extends Activity {

    Button tvJump;
    Button tvBroadcast;
    Button tvService1Start;
    Button tvService2Start;
    Button tvService1End;
    Button tvService2End;

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

        tvService1Start = (Button) findViewById(R.id.tv_service1_start);
        tvService1Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(new Intent().setComponent(
                        new ComponentName("com.jason.demoplugin", "com.jason.demoplugin.PluginService1")));
            }
        });

        tvService2Start = (Button) findViewById(R.id.tv_service2_start);
        tvService2Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(new Intent().setComponent(
                        new ComponentName("com.jason.demoplugin", "com.jason.demoplugin.PluginService2")));
            }
        });

        tvService1End = (Button)findViewById(R.id.tv_service1_stop);
        tvService1End.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(new Intent().setComponent(
                        new ComponentName("com.jason.demoplugin", "com.jason.demoplugin.PluginService1")));
            }
        });

        tvService2End = (Button)findViewById(R.id.tv_service2_stop);
        tvService2End.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(new Intent().setComponent(
                        new ComponentName("com.jason.demoplugin", "com.jason.demoplugin.PluginService2")));
            }
        });
    }
}
