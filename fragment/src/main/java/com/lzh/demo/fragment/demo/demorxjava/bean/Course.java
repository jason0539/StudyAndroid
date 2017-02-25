package com.lzh.demo.fragment.demo.demorxjava.bean;

/**
 * Created by liuzhenhui on 2016/10/29.
 */
public class Course {
    public static final String TAG = Course.class.getSimpleName();
    String name;

    public Course(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
