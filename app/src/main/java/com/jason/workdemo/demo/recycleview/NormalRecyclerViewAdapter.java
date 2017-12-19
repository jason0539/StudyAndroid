package com.jason.workdemo.demo.recycleview;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jason.workdemo.R;

/**
 * Created by liuzhenhui on 2017/12/19.
 */

public class NormalRecyclerViewAdapter extends RecyclerView.Adapter<NormalRecyclerViewAdapter.NormalTextViewHolder> {

    public static final String VIEW_TITLE_ANDROID = "Android";
    public static final String VIEW_TITLE_IOS = "iOS";

    public static final int VIEW_TYPE_ANDROID = 1;
    public static final int VIEW_TYPE_IOS = 2;

    private final LayoutInflater mLayoutInflater;
    private String[] mTitles;

    public NormalRecyclerViewAdapter(Context context) {
        mTitles = new String[]{VIEW_TITLE_ANDROID, VIEW_TITLE_IOS};
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public NormalTextViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_main_list, parent, false);
        TextView textView = (TextView) view.findViewById(R.id.tv_item_main_list);
        if (viewType == VIEW_TYPE_ANDROID) {
            textView.setTextColor(Color.GREEN);
        } else if (viewType == VIEW_TYPE_IOS) {
            textView.setTextColor(Color.RED);
        }
        return new NormalTextViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NormalTextViewHolder holder, int position) {
        holder.mTextView.setText(mTitles[position]);
    }

    @Override
    public int getItemCount() {
        return mTitles == null ? 0 : mTitles.length;
    }

    @Override
    public int getItemViewType(int position) {
        if (mTitles.length > position) {
            String title = mTitles[position];
            if (title.equals(VIEW_TITLE_ANDROID)) {
                return VIEW_TYPE_ANDROID;
            } else if (title.equals(VIEW_TITLE_IOS)) {
                return VIEW_TYPE_IOS;
            }
        }
        return super.getItemViewType(position);
    }

    public static class NormalTextViewHolder extends RecyclerView.ViewHolder {

        TextView mTextView;

        NormalTextViewHolder(View view) {
            super(view);
            mTextView = (TextView) view.findViewById(R.id.tv_item_main_list);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("NormalTextViewHolder", "onClick--> position = " + getPosition());
                }
            });
        }
    }
}
