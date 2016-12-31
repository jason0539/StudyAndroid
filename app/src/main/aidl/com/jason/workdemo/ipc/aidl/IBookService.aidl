package com.jason.workdemo.ipc.aidl;

import com.jason.workdemo.ipc.aidl.Book;

interface IBookService{
    List<Book> getBookList();
    void addBook(in Book book);
    String getBookName(int id);
}