package com.jason.common.utils;

/**
 * Created by liuzhenhui on 2016/10/28.
 */
public class CommonUtils {
    public static final String TAG = CommonUtils.class.getSimpleName();

    public static final boolean objectNull(Object object) {
        return object == null;
    }

    public static final boolean objectNotNull(Object object) {
        return object != null;
    }

    public static final boolean objOrObjNull(Object... object) {
        boolean isNull = false;
        for (Object o : object) {
            isNull |= o == null;
        }
        return isNull;
    }

    public static final boolean objAndObjNull(Object... object) {
        boolean isNull = true;
        for (Object o : object) {
            isNull &= o == null;
        }
        return isNull;
    }
}
