package com.jason.common.utils;

import android.os.Build;

/**
 * Created by liuzhenhui on 2017/2/24.
 */

public class EmulatorUtils {
    public static final boolean isEmulator() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1
                && (Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknow")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86"));
    }
}
