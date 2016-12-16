package com.jason.common.view.IndexableListView;

/**
 * Created by liuzhenhui on 2016/11/26.
 */
public class IndexModel<T> {
    String name;
    T value;

    public IndexModel(String name, T value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
