package com.jason.common.network;

/**
 * Created by liuzhenhui on 2016/10/27.
 */
public class HttpException extends RuntimeException {
    private static final String TAG = HttpException.class.getSimpleName();

    public static final int Unauthorized = 401;
    public static final int Forbidden = 403;
    public static final int NotFound = 404;

    public HttpException(int resultCode) {
        this(getHttpExceptionMessage(resultCode));
    }

    public HttpException(String detailMessage) {
        super(detailMessage);
    }

    /**
     * 由于服务器传递过来的错误信息直接给用户看的话，用户未必能够理解
     * 需要根据错误码对错误信息进行一个转换，在显示给用户
     *
     * @param code
     * @return
     */
    private static String getHttpExceptionMessage(int code) {
        String message = "";
        switch (code) {
            case Unauthorized:
                message = ":Unauthorized";
                break;
            case Forbidden:
                message = ":Forbidden";
                break;
            case NotFound:
                message = ":Not Found";
                break;
            default:
                message = ":Unknow";
                break;
        }
        return code + message;
    }

}