package com.example.dotaskapp

import android.app.Activity
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.TextView


class TaskAdapter (private val activity : Activity,  private val data: ArrayList<HashMap<String, String>>, mydb : DBHelper)
    : BaseAdapter () {

    private var database : DBHelper = mydb

    override fun getCount (): Int {
        return data.size
    }

    override fun getItem (position: Int): Any {
        return position
    }

    override fun getItemId (position: Int): Long {
        return position.toLong ()
    }

    override fun getView (position: Int, convertView: View?, parent: ViewGroup?): View {
        var holder : ListTaskViewHolder? = null
        var convertView = convertView
        if (convertView == null) {

            holder = ListTaskViewHolder ()
            convertView = LayoutInflater.from (activity).inflate (R.layout.task_list, parent, false)

            holder.task_name = convertView.findViewById (R.id.task_name)
            holder.checkBtn = convertView.findViewById (R.id.checkBtn)

            convertView.tag = holder
        } else {
            holder = convertView.tag as ListTaskViewHolder
        }

        val singleTask = data [position]
        val tmpHolder = holder

        holder.task_name!!.id = position
        holder.checkBtn!!.id = position

        try {
            holder.checkBtn!!.setOnCheckedChangeListener (null)

            if (singleTask ["status"]!!.contentEquals ("1")) {

                holder.task_name!!.text = Html.fromHtml ("<strike>" + singleTask["task"].toString() + "</strike>")
                holder.checkBtn!!.isChecked = true

            } else {
                holder.task_name!!.text = singleTask ["task"]
                holder.checkBtn!!.isChecked = false
            }

            holder.checkBtn!!.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    database.updateTaskStatus (singleTask["id"]!!, 1)
                    tmpHolder.task_name!!.text = Html.fromHtml ("<strike>" + singleTask["task"].toString() + "</strike>")
                } else {
                    database.updateTaskStatus (singleTask["id"]!!, 0)
                    tmpHolder.task_name!!.text = singleTask["task"]
                }
            }
        } catch (e : Exception) {

        }
        return convertView!!
    }
    init {
        database = mydb
    }
}

internal class ListTaskViewHolder {
    var task_name: TextView? = null
    var checkBtn: CheckBox? = null
}