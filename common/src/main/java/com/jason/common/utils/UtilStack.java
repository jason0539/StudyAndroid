package com.jason.common.utils;

/**
 * Created by liuzhenhui on 2018/1/16.
 */

public class UtilStack {
    public static final String getCallStack() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StringBuilder stringBuilder = new StringBuilder();
        for (StackTraceElement stackTraceElement : stackTrace) {
            stringBuilder.append(stackTraceElement.toString());
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
}
