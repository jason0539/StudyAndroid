package com.jason.workdemo.demo;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jason.common.utils.MLog;
import com.jason.workdemo.R;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuzhenhui on 2016/12/7.
 */
public class ListViewActivity extends Activity {
    public static final String TAG = ListViewActivity.class.getSimpleName();
    ListAdapter adapter;
    ListView listView;
    Button btnRefresh;
    List<String> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);
        listView = (ListView) findViewById(R.id.lv_demo_list);
        btnRefresh = (Button) findViewById(R.id.btn_demo_list);

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshFiles();
            }
        });
        refreshFiles();
    }

    void refreshFiles() {
        dataList = new ArrayList<>();
        String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        String[] files = Environment.getExternalStorageDirectory().list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                MLog.d(MLog.TAG_FILE, TAG + "->" + "accept filename = " + filename);
                if (filename.contains("g") || filename.contains("GP")) {
                    MLog.d(MLog.TAG_FILE, TAG + "->" + "accept " + filename);
                    return true;
                }
                return false;
            }
        });
        for (String gp : files) {
            MLog.d(MLog.TAG_FILE, TAG + "->" + "refreshFiles " + gp);
            dataList.add(rootPath + File.separator + gp);
        }

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataList) {
            @NonNull
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                ((TextView) view.findViewById(android.R.id.text1)).setTextColor(Color.BLACK);
                return view;
            }
        };
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                viewScore(dataList.get(position));
            }
        });
    }

    void viewScore(String path) {
        MLog.d(MLog.TAG_FILE, TAG + "->" + "viewScore " + path);
    }
}
