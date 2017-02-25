package com.lzh.demo.plugin.contentprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.jason.common.utils.MLog;

/**
 * Created by liuzhenhui on 2017/2/19.
 * 代理ContentProvider
 * 外部应用想要使用宿主内插件提供的ContentProvider时，需要遵循宿主提供的调用协议，所有查询都会进入这里，从这里分发到插件
 * 由于插件中ContentProvider的安装是在HookActivity中进行的，所以外部调用插件ContentProvider时需要HookActivity执行过安装过程
 * （正常应该把插件安装过程放在Application的onAttachBase中，Demo中为了保证各部分的独立，所有插件相关操作都在HookActivity中）
 * http://weishu.me/2016/07/12/understand-plugin-framework-content-provider/
 */

public class ProxyContentProvider extends ContentProvider {

    public static final String AUTHORITY = "com.jason.workdemo";

    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return getContext().getContentResolver().query(parseUri(uri), projection, selection, selectionArgs, sortOrder);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return getContext().getContentResolver().insert(parseUri(uri), values);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    /**
     * 为了使得插件的ContentProvder提供给外部使用，我们需要一个ProxyContentProvider做中转；
     * 如果外部程序需要使用插件系统中插件的ContentProvider，不能直接查询原来的那个uri
     * 我们对uri做一些手脚，使得插件系统能识别这个uri；
     * <p>
     * 这里的处理方式如下：
     * <p>
     * 原始查询插件的URI应该为：
     * content://plugin_auth/path/query
     * <p>
     * 如果需要查询插件，需要修改为：
     * <p>
     * content://proxy_auth/plugin_auth/path/query
     * <p>
     * 也就是，我们把插件ContentProvider的信息放在URI的path中保存起来；
     * 然后在ProxyContentProvider中做分发。
     * <p>
     * 当然，也可以使用QueryParamerter,比如：
     * content://proxy_auth/path/query/ ->  content://proxy_auth/path/query?plugin=plugin_auth
     */
    private Uri parseUri(Uri proxyUri) {
        MLog.d(MLog.TAG_HOOK, "ProxyContentProvider->parseUri proxyUri = " + proxyUri);
        String proxyAuth = proxyUri.getAuthority();
        String uriString = proxyUri.toString();
        uriString = uriString.replace(proxyAuth + '/', "");
        Uri realUri = Uri.parse(uriString);
        MLog.d(MLog.TAG_HOOK, "ProxyContentProvider->parseUri realUri = " + realUri);
        return realUri;
    }
}
