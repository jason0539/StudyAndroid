package com.jason.workdemo.scheme;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import com.jason.workdemo.R;

/**
 * liuzhenhui 16/6/4.上午10:01
 */
public class SchemeDemoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheme);
        findViewById(R.id.test_action_browser).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBrowser();
            }
        });
    }

    private void openBrowser() {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(
                "baidumap://map/component?comName=lbc&target=webshell_login_page&param={\"url\":\"http://cq01-rdqa"
                        + "-dev036.cq01.baidu.com:8001/intergration/\",\"needLogin\":\"0\"}");
        intent.setData(content_url);
        startActivity(intent);
    }

    private void openDial() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DIAL);
        //        String uri = Uri.encode();
        intent.setData(Uri.parse("tel:10086,,1,,1"));
        startActivity(intent);
    }
}
