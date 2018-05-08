package com.jason.workdemo.kotlin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView

class AnotherKotlinActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tvMain = TextView(this)
        tvMain.text = "Hello, world"
        setContentView(tvMain)
        var user: User? = intent?.getSerializableExtra(USER) as? User
        user?.let { toast(user.name) }

    }

    companion object {
        val USER = "USER"
        fun startActivity(context: Context, user: User) {
            val intent = Intent(context, AnotherKotlinActivity::class.java)
            intent.putExtra(USER, user)
            context.startActivity(intent)
        }
    }
}
