package com.jason.common.view.IndexableListView;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.jason.common.R;

import java.util.List;

/**
 * Created by liuzhenhui on 2016/11/26.
 */
public class IndexableAdapter extends ArrayAdapter<IndexModel<String>> implements SectionIndexer {
    private static final String TAG = IndexableAdapter.class.getSimpleName();

    private String mSections = "#ABCDEFGHIJKLNOPQRSTUVWXYZ";
    private static int layoutResId = R.layout.item_indexable_listview;

    public IndexableAdapter(Context context, List<IndexModel<String>> objects, String mSections) {
        super(context, layoutResId, objects);
        if (!TextUtils.isEmpty(mSections)) {
            this.mSections = mSections;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        IndexModel<String> model = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(layoutResId, null);
            viewHolder = new ViewHolder();
            viewHolder.mName = (TextView) view.findViewById(R.id.tv_item_indexable_name);
            viewHolder.mLine = view.findViewById(R.id.view_item_indexable_line);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.mName.setText(model.getName());
        if (position == getCount()-1) {
            viewHolder.mLine.setVisibility(View.INVISIBLE);
        }
        return view;
    }

    @Override
    public int getPositionForSection(int section) {
        for (int i = section; i >= 0; i--) {
            for (int j = 0; j < getCount(); j++) {
                if (i == 0) {
                    for (int k = 0; k <= 9; k++) {
                        if (StringMatcher.match(String.valueOf(getItem(j).getName().charAt(0)), String.valueOf(k))) {
                            return j;
                        }
                    }
                } else {
                    if (StringMatcher.match(String.valueOf(getItem(j).getName().charAt(0)), String.valueOf(mSections.charAt(i)))) {
                        return j;
                    }
                }
            }
        }
        return 0;
    }

    @Override
    public int getSectionForPosition(int position) {
        return 0;
    }

    @Override
    public Object[] getSections() {
        String[] sections = new String[mSections.length()];
        for (int i = 0; i < mSections.length(); i++) {
            sections[i] = String.valueOf(mSections.charAt(i));
        }
        return sections;
    }

    class ViewHolder {
        TextView mName;
        View mLine;
    }
}