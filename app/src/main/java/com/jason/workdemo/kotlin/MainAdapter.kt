package com.jason.workdemo.kotlin

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.jason.workdemo.R
import kotlinx.android.synthetic.main.item_main_list.view.*

class MainAdapter : RecyclerView.Adapter<MainAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        var view: View = View.inflate(parent?.context, R.layout.item_main_list, null)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 50
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        with(holder?.itemView!!) {
            tv_item_main_list.text = "第${position}条数据"
        }
    }

    class ViewHolder(itemview: View?) : RecyclerView.ViewHolder(itemview)
}