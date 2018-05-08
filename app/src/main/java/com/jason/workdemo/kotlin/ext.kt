package com.jason.workdemo.kotlin

import android.content.Context
import android.widget.Toast

fun Context.toast(msg: String?, duration: Int = Toast.LENGTH_SHORT) {
    msg?.let { Toast.makeText(this, msg, Toast.LENGTH_SHORT).show() }

}