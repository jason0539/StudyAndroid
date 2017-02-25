package com.lzh.demo.fragment.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jason.common.utils.MLog;
import com.lzh.demo.fragment.FragmentMainActivity;

/**
 * Created by liuzhenhui on 2016/10/27.
 */
public class BaseFragment extends Fragment {
    private final static String TAG = BaseFragment.class.getSimpleName();

    protected static FragmentMainActivity mActivity;
    protected static JLFragmentManager jlFragmentManager;
    protected static Context mContext;
    protected ViewGroup mContainer;
    protected static LayoutInflater mInflater;

    public static void initBeforeAll(FragmentMainActivity activity, JLFragmentManager fragmentManager) {
        mActivity = activity;
        mContext = mActivity.getApplicationContext();
        jlFragmentManager = fragmentManager;
        mInflater = mActivity.getLayoutInflater();
    }

    public static JLFragmentManager getJLFragmentManager() {
        return jlFragmentManager;
    }

    public static LayoutInflater getInflater() {
        return mInflater;
    }

    public boolean canProcessUI() {
        return isAdded();
    }

    public static FragmentMainActivity getMainActivity() {
        return mActivity;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public void onAttach(Activity activity) {
        MLog.d(MLog.TAG_FRAGMENT, TAG + "->" + "onAttach ");
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        MLog.d(MLog.TAG_FRAGMENT, TAG + "->" + "onCreate ");
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true); // 允许fragment修改menu
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MLog.d(MLog.TAG_FRAGMENT, TAG + "->" + "onCreateView ");
        mContainer = container;
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        MLog.d(MLog.TAG_FRAGMENT, TAG + "->" + "onActivityCreated ");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        MLog.d(MLog.TAG_FRAGMENT, TAG + "->" + "onStart ");
        super.onStart();
    }

    @Override
    public void onResume() {
        MLog.d(MLog.TAG_FRAGMENT, TAG + "->" + "onResume ");
        super.onResume();
    }

    @Override
    public void onPause() {
        MLog.d(MLog.TAG_FRAGMENT, TAG + "->" + "onPause ");
        super.onPause();
    }

    @Override
    public void onStop() {
        MLog.d(MLog.TAG_FRAGMENT, TAG + "->" + "onStop ");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        MLog.d(MLog.TAG_FRAGMENT, TAG + "->" + "onDestroyView ");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        MLog.d(MLog.TAG_FRAGMENT, TAG + "->" + "onDestroy ");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        MLog.d(MLog.TAG_FRAGMENT, TAG + "->" + "onDetach ");
        super.onDetach();
    }

}
