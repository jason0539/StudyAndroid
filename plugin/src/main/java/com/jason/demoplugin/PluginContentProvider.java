package com.jason.demoplugin;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by liuzhenhui on 2017/2/13.
 */

public class PluginContentProvider extends ContentProvider {

    private DatabaseHelper mDatabaseHelper;
    private static final UriMatcher mUriMatcher;
    private static final String[] sTestPluginProjection;

    public static final String AUTHORITY = "com.jason.demoplugin.provider";
    public static final String PATH = "plugin";
    public static final String PATH_NUMBER = "plugin/#";
    public static final Uri PLUGIN_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + PATH);

    private static final int TEST_PLUGIN = 101;
    private static final int TEST_PLUGIN_ID = 102;

    static {
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        //匹配AUTHORITY/PATH
        mUriMatcher.addURI(AUTHORITY, PATH, TEST_PLUGIN);
        //匹配AUTHORITY/PATH_NUMBER
        mUriMatcher.addURI(AUTHORITY, PATH_NUMBER, TEST_PLUGIN_ID);
        sTestPluginProjection = new String[]{
                PluginTable._ID, PluginTable.PLUGIN_NAME, PluginTable.CREATE_DATE, PluginTable.MODIFIED_DATE
        };
    }

    @Override
    public boolean onCreate() {
        Log.d("TAG_HOOK", "PluginContentProvider -> onCreate");
        mDatabaseHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        Log.d("TAG_HOOK", "PluginContentProvider -> getType");
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Log.d("TAG_HOOK", "PluginContentProvider -> insert : uri = " + uri);
        String tableName;
        String nullColumnHack;
        Resources resources = Resources.getSystem();
        if (values == null) {
            values = new ContentValues();
        }

        Long now = Long.valueOf(System.currentTimeMillis());

        switch (mUriMatcher.match(uri)) {
            case TEST_PLUGIN:
                tableName = PluginTable.TABLE_NAME;
                nullColumnHack = PluginTable.PLUGIN_NAME;
                if (!values.containsKey(PluginTable.PLUGIN_NAME)) {
                    values.put(PluginTable.PLUGIN_NAME, resources.getString(android.R.string.untitled));
                }
                if (!values.containsKey(PluginTable.CREATE_DATE)) {
                    values.put(PluginTable.CREATE_DATE, now);
                }
                if (!values.containsKey(PluginTable.MODIFIED_DATE)) {
                    values.put(PluginTable.MODIFIED_DATE, now);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknow URI " + uri);
        }
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        long rowId = db.insert(tableName, nullColumnHack, values);
        if (rowId > 0) {
            Log.d("TAG_HOOK", "PluginContentProvider -> insert 成功 " + rowId);
            Uri returnUri = ContentUris.withAppendedId(uri, rowId);
            getContext().getContentResolver().notifyChange(returnUri, null);
            return returnUri;
        }else {
            Log.d("TAG_HOOK", "PluginContentProvider -> insert 失败 " + rowId);
        }
        throw new SQLException("Failed to insert row into " + uri);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        Log.d("TAG_HOOK", "PluginContentProvider -> delete");
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        String tableName;

        int count;

        switch (mUriMatcher.match(uri)) {
            case TEST_PLUGIN:
                tableName = PluginTable.TABLE_NAME;
                break;
            case TEST_PLUGIN_ID:
                tableName = PluginTable.TABLE_NAME;
                selection = PluginTable._ID + "=" + uri.getPathSegments().get(1)
                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : "");
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        count = db.delete(tableName, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        Log.d("TAG_HOOK", "PluginContentProvider -> update");
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        Long now = Long.valueOf(System.currentTimeMillis());

        String tableName;
        int count;

        switch (mUriMatcher.match(uri)) {
            case TEST_PLUGIN:
                tableName = PluginTable.TABLE_NAME;
                if (!values.containsKey(PluginTable.MODIFIED_DATE)) {
                    values.put(PluginTable.MODIFIED_DATE, now);
                }
                break;
            case TEST_PLUGIN_ID:
                tableName = PluginTable.TABLE_NAME;
                if (!values.containsKey(PluginTable.MODIFIED_DATE)) {
                    values.put(PluginTable.MODIFIED_DATE, now);
                }
                selection = PluginTable._ID + "=" + uri.getPathSegments().get(1)
                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : "");
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        count = db.update(tableName, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Log.d("TAG_HOOK", "PluginContentProvider -> query : uri = " + uri + ", selection = " + selection + ", projection = " + projection + ", selectionArgs = " + selectionArgs + ",sortOrder = " + sortOrder);
        String tableName;
        switch (mUriMatcher.match(uri)) {
            case TEST_PLUGIN:
                tableName = PluginTable.TABLE_NAME;
                if (projection == null || projection.length == 0) {
                    projection = sTestPluginProjection;
                }
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = PluginTable.SORT_ORDER;
                }
                break;
            case TEST_PLUGIN_ID:
                tableName = PluginTable.TABLE_NAME;
                if (projection == null || projection.length == 0) {
                    projection = sTestPluginProjection;
                }
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = PluginTable.SORT_ORDER;
                }
                if (TextUtils.isEmpty(selection)) {
                    selection = PluginTable._ID + " = " + uri.getPathSegments().get(1);
                } else {
                    selection += " AND " + PluginTable._ID + " = " + uri.getPathSegments().get(1);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
        Cursor cursor = db.query(tableName, projection, selection, selectionArgs, null, null, sortOrder);
        return cursor;
    }


    public static final class PluginTable implements BaseColumns {
        //表名
        public static final String TABLE_NAME = "plugin_table";
        //表的属性
        public static final String PLUGIN_NAME = "plugin_name";
        public static final String CREATE_DATE = "create_date";
        public static final String MODIFIED_DATE = "modified_date";
        public static final String SORT_ORDER = "modified_date DESC";
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        //数据库名和版本
        public static final String DATABASE_NAME = "plugin_database.db";
        public static final int DATABASE_VERSION = 2;

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            Log.d("TAG_HOOK", "DatabaseHelper -> DatabaseHelper");
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String path = db.getPath();
            Log.d("TAG_HOOK", "DatabaseHelper -> onCreate : db.path = " + path);
            db.execSQL("CREATE TABLE IF NOT EXISTS " + PluginTable.TABLE_NAME + "("
                    + PluginTable._ID + " INTEGER PRIMARY KEY, "
                    + PluginTable.PLUGIN_NAME + " TEXT, "
                    + PluginTable.CREATE_DATE + " INTEGER, "
                    + PluginTable.MODIFIED_DATE + " INTEGER );");
        }

        @Override
        public void onOpen(SQLiteDatabase db) {
            Log.d("TAG_HOOK", "DatabaseHelper -> onOpen");
            db.execSQL("CREATE TABLE IF NOT EXISTS " + PluginTable.TABLE_NAME + "("
                    + PluginTable._ID + " INTEGER PRIMARY KEY, "
                    + PluginTable.PLUGIN_NAME + " TEXT, "
                    + PluginTable.CREATE_DATE + " INTEGER, "
                    + PluginTable.MODIFIED_DATE + " INTEGER );");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.d("TAG_HOOK", "DatabaseHelper -> onUpgrade");
            //trick，直接删除重建
            db.execSQL("DROP TABLE IF EXISTS " + PluginTable.TABLE_NAME);
            onCreate(db);
        }
    }
}
