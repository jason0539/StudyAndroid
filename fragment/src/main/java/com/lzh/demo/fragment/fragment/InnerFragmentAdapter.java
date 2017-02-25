package com.lzh.demo.fragment.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by liuzhenhui on 2016/11/14.
 */
public class InnerFragmentAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> mFragmentList;

    public InnerFragmentAdapter(FragmentManager fm, ArrayList<Fragment> list) {
        super(fm);
        mFragmentList = list;
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }
}