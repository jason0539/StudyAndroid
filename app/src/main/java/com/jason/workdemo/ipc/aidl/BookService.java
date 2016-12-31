package com.jason.workdemo.ipc.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.jason.common.utils.MLog;

import java.util.List;

/**
 * Created by liuzhenhui on 2016/12/31.
 */
public class BookService extends Service {
    public static final String TAG = BookService.class.getSimpleName();

    private IBinder mBinder = new IBookService.Stub() {

        @Override
        public List<Book> getBookList() throws RemoteException {
            MLog.d(MLog.TAG_AIDL, "BookService->" + "getBookList ");
            return null;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            MLog.d(MLog.TAG_AIDL, "BookService->" + "addBook ");
        }

        @Override
        public String getBookName(int id) throws RemoteException {
            MLog.d(MLog.TAG_AIDL, "BookService->" + "getBookName id = " + id);
            return "Hello,book";
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        MLog.d(MLog.TAG_AIDL,"BookService->"+"onBind ");
        return mBinder;
    }
}
