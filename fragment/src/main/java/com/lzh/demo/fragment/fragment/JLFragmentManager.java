package com.lzh.demo.fragment.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.lzh.demo.fragment.HomeFragment;
import com.lzh.demo.fragment.SplashFragment;
import com.lzh.demo.fragment.demo.demoglide.DemoGlideFragment;
import com.lzh.demo.fragment.demo.demoretrofit.RxRetrofitFragment;
import com.lzh.demo.fragment.demo.demorxjava.RxJavaDemoFragment;

/**
 * Created by liuzhenhui on 2016/10/27.
 */
public class JLFragmentManager extends ContentFragmentManager implements
        IContentFragmentFactory {
    private static final String TAG = JLFragmentManager.class.getSimpleName();

    /* 无效值 */
    public final static int TYPE_NONE = 0x0000;
    /* 1x: 页面 */
    public final static int TYPE_HOME = 0x0011;
    /* 2x: 测试页面 */
    public final static int TYPE_RXRTROFIT = 0x0021;
    public static final int TYPE_RXJAVA = 0x0022;
    public static final int TYPE_REALM = 0x0023;
    public static final int TYPE_GLIDE = 0x0024;
    public static final int TYPE_ERROR = 0x0025;
    public static final int TYPE_TEST = 0x0026;
    /* 2x: 其他页面 */
    public final static int TYPE_SPLASH = 0x0041;// splash
    public final static int TYPE_LOGIN = 0x0042;
    public final static int TYPE_REGISTER = 0x0043;
    public final static int TYPE_REGISTER_INFO = 0x0045;
    public final static int TYPE_FORGET = 0x0046;
    public final static int TYPE_RESET_PASSWORD = 0x0047;
    public final static int TYPE_PROFILE = 0x0048;
    public final static int TYPE_COUNTRY_CODE = 0x0049;

    /**
     * 前一个fragment 类型 ，即从哪个fragment跳转过来的
     */
    private int mPreviousFragmentType = TYPE_NONE;


    public JLFragmentManager(FragmentActivity activity) {
        super(activity);
        setFragmentFactory(this);
    }

    @Override
    public ContentFragment createFragment(int type) {
        ContentFragment fragment = null;
        switch (type) {
            case TYPE_SPLASH:
                fragment = new SplashFragment();
                break;
            case TYPE_HOME:
                fragment = new HomeFragment();
                break;
            case TYPE_RXRTROFIT:
                fragment = new RxRetrofitFragment();
                break;
            case TYPE_GLIDE:
                fragment = new DemoGlideFragment();
                break;
            case TYPE_RXJAVA:
                fragment = new RxJavaDemoFragment();
                break;
            default:
                fragment = new ErrorFragment();
                break;
        }
        return fragment;
    }

    @Override
    public String toString(int type) {
        String str = null;
        switch (type) {
        /* 1x: 地图相关页面 */
            case TYPE_HOME:
                str = "TYPE_HOME";
                break;

		/* 无效值 */
            default:
                str = "TYPE_NONE";
                break;
        }

        return str;
    }

    public int getCurrentFragmentType() {
        if (null != mCurrentFragmentInfo
                && null != mCurrentFragmentInfo.mFragment) {
            return mCurrentFragmentInfo.mFragment.getType();
        }
        return TYPE_NONE;
    }

    /**
     * 获取前一个fragment类型，即从哪个fragment跳转过来的
     */
    public int getPreviousFragmentType() {
        return mPreviousFragmentType;
    }

    public void backTo(int type){
        backTo(type, null);
    }

    /**
     * 回退到type类型的fragment
     */
    public void backTo(int type, Bundle bundle) {
        while (mFragmentInfoStack.size() > 0) {
            if (mFragmentInfoStack.get(mFragmentInfoStack.size() - 1).mType == type) {
                back(bundle);
                break;
            } else {
                removeFragmentFromStack(mFragmentInfoStack.size() - 1);
            }
        }
    }

    /**
     * 清理HOME页之后压栈的所有fragment，仅用于导航过程页(真实导航)启动前调用。
     */
    public void removeFragmentTo(int type) {
        int index = getIndexFromLast(type);
        if (index >= 0) {
            int stackSize = mFragmentInfoStack.size();
            for (int i = stackSize - 1; i > index; i--) {
                removeFragmentFromStack(i);
            }
        }
    }

    public void showFragment(int type) {
        showFragment(type, null);
    }

    @Override
    public void showFragment(int type, Bundle bundle) {
        super.showFragment(type, bundle);
    }

    /**
     * 判断type类型的fragment是否MapContentFragment
     *
     * @param type fragment类型
     * @return 是否MapContentFragment
     */
    public boolean isMapContent(int type) {
        boolean isMapContentFragment = false;
        switch (type) {
            case TYPE_SPLASH:
                isMapContentFragment = false;
                break;

            default:
                break;
        }

        return isMapContentFragment;
    }

    /**
     * 从后往前，获取第一个类型是type的fragment在栈中的下标
     *
     * @param type fragment类型
     * @return 下标
     */
    private int getIndexFromLast(int type) {
        int i = mFragmentInfoStack.size() - 1;
        for (; i >= 0; i--) {
            if (mFragmentInfoStack.get(i).mType == type) {
                break;
            }
        }

        return i;
    }
}
