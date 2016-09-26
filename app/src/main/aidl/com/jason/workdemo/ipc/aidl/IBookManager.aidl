package com.jason.workdemo.ipc.aidl;

import com.jason.workdemo.ipc.aidl.Book;

interface IBookManager{
    List<Book> getBookList();
    void addBook(in Book book);
}