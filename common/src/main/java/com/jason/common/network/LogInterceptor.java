package com.jason.common.network;


import com.jason.common.utils.MLog;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * Created by huangxu on 11/28/16.
 */
public class LogInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        long t1 = System.nanoTime();
        RequestBody body = request.body();
        String bodyString = null;
        if (body != null && body.contentType().subtype().equals("json")) {
            final Buffer buffer = new Buffer();
            body.writeTo(buffer);
            bodyString = buffer.readUtf8();
            buffer.close();
        }
        MLog.d(MLog.TAG_HTTP, String.format("Request Url:%s%n%s%n%s%s",
                request.url(), request.method(), request.headers(), bodyString));
        Response response = chain.proceed(request);

        long t2 = System.nanoTime();
        MLog.d(MLog.TAG_HTTP, String.format("Received response for %s in %.1fms%n%s",
                response.request().url(), (t2 - t1) / 1e6d, response.headers()));


        return response;
    }
}
