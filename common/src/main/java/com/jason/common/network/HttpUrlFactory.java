package com.jason.common.network;

/**
 * Created by liuzhenhui on 2016/11/15.
 */
public class HttpUrlFactory {
    public static final String TAG = HttpUrlFactory.class.getSimpleName();
    public static boolean isDebug = true;

    //122.224.168.22:9001/
    //http://192.168.3.18/
    public static final String TEST_URL = "http://192.168.3.18/";
    public static final String STABLE_URL = "";

    //    Login ANd Register
    public static final String CheckAccountExits = "api/v1.0/services/check/accounts";
    public static final String SendSmsVerify = "api/v1.0/users/send_verify_code";
    public static final String VerifySmsCode = "api/v1.0/users/check_verify_code";
    public static final String SignUp = "api/v1.3/users/signup";
    public static final String SignIn = "api/v1.0/oauth/token";
    public static final String ResetPassword = "api/v1.1/users/resetpw";

    public static String getUrl() {
        return isDebug ? TEST_URL : STABLE_URL;
    }

}
