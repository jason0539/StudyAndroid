package com.jason.demoplugin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

//继承AppCompat的方式导致classnotfound
//public class MainActivity extends AppCompatActivity {
public class MainActivity extends Activity {

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
