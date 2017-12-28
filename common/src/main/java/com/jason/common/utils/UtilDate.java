package com.jason.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by liuzhenhui on 2017/12/28.
 */

public class UtilDate {
    public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";
    private static final String DEFAULT_DATETIME_PATTERN = "yyyy-MM-dd hh:mm:ss";

    public static final String formatDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat(DEFAULT_DATE_PATTERN);
        return format.format(date);
    }
}
