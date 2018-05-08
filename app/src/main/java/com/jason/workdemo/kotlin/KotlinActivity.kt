package com.jason.workdemo.kotlin

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.jason.common.utils.MLog
import com.jason.workdemo.R
import kotlinx.android.synthetic.main.activity_kotlin.*

class KotlinActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin)
        btn_kotlin_jump.setOnClickListener {
            val intent = Intent(this, AnotherKotlinActivity::class.java)
            startActivity(intent)
        }
//        studyVar()
        var adapter = MainAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

    }

    fun studyVar() {
        //=======变量声明==========
        var a: String = "abc"//不可赋空值
        var b: String? = "abc"//可赋空值

        //=======变量赋值===========
        //赋予空值，编译报错
        //a = null
        //可以赋予空值
        b = null

        //=======变量属性访问========
        //此时访问a的属性保证不会npe
        var al = a.length
        //访问可赋空值的b则会报错误
        //var bl = b.length

        //======空值检查=======
        //访问b要处理值为空的情况
        //条件判断
        var l = if (b != null) b.length else -1
        //安全调用,如果 b 非空，就返回 b.length，否则返回 null，这个表达式的类型是 Int?。
        b?.length
        //也可以用？结合let在非空时执行一些逻辑
        b?.let { MLog.d(MLog.TAG, "b不为空") }

        //=======Elvis操作符（?:）========
        var bLength: Int = if (b != null) b.length else -1
        //上面操作等同于下，如果 ?: 左侧表达式非空，elvis 操作符就返回其左侧表达式，否则返回右侧表达式
        bLength = b?.length ?: -1

        //========!! 操作符=======
        //非空断言运算符（!!）将任何值转换为非空类型，若该值为空则抛出异常,如果你想要一个 NPE，你可以得到它，但是你必须显式要求它，否则它不会不期而至。
        val bl = b!!.length

        //=======安全的类型转换======
        //如果尝试转换不成功，则返回null
        val aInt: Int? = a as? Int

        //可空类型的集合
        val nullableList: List<Int?> = listOf(1, 2, null, 4)
        val intList: List<Int> = nullableList.filterNotNull()

        //
        var user = User("name")
        var userId: String = user?.id ?: "null"
        MLog.d(MLog.TAG, "用户id为${userId}")
    }
}
