package com.jason.workdemo.page.demo.demorxjava.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuzhenhui on 2016/10/29.
 */
public class Student {
    public static final String TAG = Student.class.getSimpleName();
    String name;
    List<Course> courses;

    public Student(String name) {
        this.name = name;
        courses = new ArrayList<Course>();
        courses.add(new Course("math"));
        courses.add(new Course("english"));
        courses.add(new Course("program"));
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
