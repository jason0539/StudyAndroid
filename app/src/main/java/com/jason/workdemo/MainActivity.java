package com.jason.workdemo;

import android.app.Activity;
import android.content.Intent;
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
import com.jason.workdemo.animation.AnimActivity;
import com.jason.workdemo.animation.ScrollActivity;
import com.jason.workdemo.demo.ListViewActivity;
import com.jason.workdemo.demo.TextViewActivity;
import com.jason.workdemo.demo.scroller.ScrollerActivity;
import com.jason.workdemo.demo.span.SpanDemoActivity;
import com.jason.workdemo.demo.webview.WebviewActivity;
import com.jason.workdemo.page.FragmentMainActivity;
import com.jason.workdemo.view.CustomViewActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * liuzhenhui 16/5/5.下午9:55
 */
public class MainActivity extends Activity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView mListView = (ListView) findViewById(R.id.lv_main_list);

        final List<Class> dataList = new ArrayList<>();
        dataList.add(CustomViewActivity.class);
        dataList.add(AnimActivity.class);
        dataList.add(ScrollActivity.class);
        dataList.add(ListViewActivity.class);
        dataList.add(SpanDemoActivity.class);
        dataList.add(TextViewActivity.class);
        dataList.add(ScrollerActivity.class);
        dataList.add(FragmentMainActivity.class);
        dataList.add(WebviewActivity.class);
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