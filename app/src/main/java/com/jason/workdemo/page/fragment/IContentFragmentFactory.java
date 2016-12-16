package com.jason.workdemo.page.fragment;

/**
 * Created by liuzhenhui on 2016/10/27.
 */
public interface IContentFragmentFactory {

    /**
     * 根据类型产生fragment
     *
     * @param type fragment类型
     * @return fragment
     */
    ContentFragment createFragment(int type);

    /**
     * fragment类型对应的名称字符串
     *
     * @param type fragment类型
     * @return 类型对应的名称
     */
    String toString(int type);
}

