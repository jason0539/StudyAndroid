package com.jason.workdemo.ipc.aidl;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;

import com.jason.common.utils.MLog;

/**
 * Created by liuzhenhui on 2016/12/31.
 */
public class AIDLActivity extends Activity {
    public static final String TAG = AIDLActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Button btnAidl = new Button(this);
        btnAidl.setText("aidl");
        btnAidl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MLog.d(MLog.TAG_AIDL,"AIDLActivity->"+"onClick ");
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("com.jason.workdemo",
                        "com.jason.workdemo.ipc.aidl.BookService"));
                boolean result = bindService(intent, mBookServiceConnection, BIND_AUTO_CREATE);
                MLog.d(MLog.TAG_AIDL,"AIDLActivity->"+"onClick result = " + result);
            }
        });
        setContentView(btnAidl);
    }

    private ServiceConnection mBookServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MLog.d(MLog.TAG_AIDL,"AIDLActivity->"+"onServiceConnected ");
            IBookService iBookService = IBookService.Stub.asInterface(service);
            try {
                String bookName = iBookService.getBookName(1);
                MLog.d(MLog.TAG_AIDL,"AIDLActivity->"+"getBookName : " + bookName);
            } catch (RemoteException e) {
                MLog.d(MLog.TAG_AIDL,"AIDLActivity->"+"onServiceConnected e = " + e.toString());
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            MLog.d(MLog.TAG_AIDL,"AIDLActivity->"+"onServiceDisconnected ");
        }
    };
}
