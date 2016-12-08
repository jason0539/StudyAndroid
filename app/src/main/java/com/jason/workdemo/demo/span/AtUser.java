package com.jason.workdemo.demo.span;

import java.io.Serializable;

/**
 * Created by liuzhenhui on 2016/12/8.
 */
public class AtUser implements Serializable {

    public long userId;

    public String userNick;

    public AtUser() {
    }

    public AtUser(long userId, String userNick) {
        this.userId = userId;
        this.userNick = userNick;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserNick() {
        return userNick;
    }

    public void setUserNick(String userNick) {
        this.userNick = userNick;
    }
}
