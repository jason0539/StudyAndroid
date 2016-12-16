package com.jason.common.network.http;

/**
 * Created by liuzhenhui on 2016/11/16.
 */
public class HttpResult<T> {
    public static final String TAG = HttpResult.class.getSimpleName();
    private T res;

    private int status;

    private String msg;

    public T getRes() {
        return res;
    }

    public void setRes(T res) {
        this.res = res;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "CheckExitsHttpResult [res = " + res.toString() + ", status = " + status + ", msg = " + msg + "]";
    }
}
