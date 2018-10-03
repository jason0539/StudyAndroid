package com.jason.workdemo;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jason.common.utils.MLog;
import com.jason.common.utils.MToast;
import com.jason.workdemo.animation.AnimationActivity;
import com.jason.workdemo.demo.BitmapDemoActivity;
import com.jason.workdemo.demo.DemoCaseActivity;
import com.jason.workdemo.demo.SchemeDemoActivity;
import com.jason.workdemo.demo.StorageActivity;
import com.jason.workdemo.demo.TextViewActivity;
import com.jason.workdemo.demo.dragger.ViewDraggerActivity;
import com.jason.workdemo.demo.json.JsonDemoActivity;
import com.jason.workdemo.demo.memory.MemoryLeakActivity;
import com.jason.workdemo.demo.recycleview.RecycleViewDemo;
import com.jason.workdemo.demo.rxbus.RxBusActivityA;
import com.jason.workdemo.ipc.aidl.AIDLActivity;
import com.jason.workdemo.kotlin.KotlinActivity;
import com.jason.workdemo.view.CustomViewActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * liuzhenhui 16/5/5.下午9:55
 */
public class MainActivity extends Activity {
    private static final String TAG = MainActivity.class.getSimpleName();

    public static final int WRITE_REQUEST_CODE = 110;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MToast.init(getApplicationContext());

        ListView mListView = (ListView) findViewById(R.id.lv_main_list);

        final List<Class> dataList = new ArrayList<>();
        dataList.add(CustomViewActivity.class);
//        dataList.add(AnimActivity.class);
        dataList.add(AnimationActivity.class);
//        dataList.add(ListViewActivity.class);
//        dataList.add(SpanDemoActivity.class);
//        dataList.add(TextViewActivity.class);
//        dataList.add(ScrollerActivity.class);
//        dataList.add(FragmentMainActivity.class);
//        dataList.add(WebviewActivity.class);
        dataList.add(TextViewActivity.class);
        dataList.add(AIDLActivity.class);
        dataList.add(SchemeDemoActivity.class);
        dataList.add(StorageActivity.class);
        dataList.add(KotlinActivity.class);
        dataList.add(DemoCaseActivity.class);
        dataList.add(JsonDemoActivity.class);
        dataList.add(RxBusActivityA.class);
        dataList.add(RecycleViewDemo.class);
        dataList.add(ViewDraggerActivity.class);
        dataList.add(BitmapDemoActivity.class);
        dataList.add(MemoryLeakActivity.class);
        ListAdapter mAdapter = new SimpleAdapter(dataList);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, dataList.get(position));
                startActivity(intent);
            }
        });
//        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
//        requestPermissions(permissions, WRITE_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case WRITE_REQUEST_CODE:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    MToast.showSuccess("权限获取成功");
                } else{
                    MToast.show("权限获取失败");
                }
                break;
        }
    }

    class SimpleAdapter extends BaseAdapter {
        List<Class> mClassList;

        SimpleAdapter(List<Class> list) {
            mClassList = list;
        }

        @Override
        public int getCount() {
            return mClassList.size();
        }

        @Override
        public Object getItem(int position) {
            return mClassList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MLog.d(MLog.TAG, TAG + "->" + "getView ");
            View view;
            ViewHolder viewHolder;
            if (convertView == null) {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main_list, null);
                viewHolder = new ViewHolder();
                viewHolder.mTvClassName = (TextView) view.findViewById(R.id.tv_item_main_list);
                view.setTag(viewHolder);
            } else {
                view = convertView;
                viewHolder = (ViewHolder) view.getTag();
            }
            viewHolder.mTvClassName.setText(mClassList.get(position).getSimpleName().toString());
            return view;
        }

        class ViewHolder {
            TextView mTvClassName;
        }
    }
}