package com.jason.workdemo.kotlin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.jason.workdemo.R
import kotlinx.android.synthetic.main.activity_kotlin.*

class KotlinActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin)
        btn_kotlin_jump.setOnClickListener {
            val intent = Intent(this,AnotherKotlinActivity::class.java)
            startActivity(intent)
        }
    }
}
