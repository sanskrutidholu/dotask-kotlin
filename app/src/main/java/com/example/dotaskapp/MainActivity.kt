package com.example.dotaskapp

import android.content.Intent
import android.database.Cursor
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView


class MainActivity : AppCompatActivity() {

    private lateinit var mydb: DBHelper
    private lateinit var empty: LinearLayout
    private lateinit var scrollView: NestedScrollView
    private lateinit var todayContainer: LinearLayout
    private lateinit var tomorrowContainer: LinearLayout
    private lateinit var otherContainer: LinearLayout
    private lateinit var taskListToday: NoScrollListView
    private lateinit var taskListTomorrow: NoScrollListView
    private lateinit var taskListUpcoming: NoScrollListView

    var todayList = ArrayList<HashMap<String, String>>()
    var tomorrowList = ArrayList<HashMap<String, String>>()
    var upcomingList = ArrayList<HashMap<String, String>>()

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        setContentView(R.layout.activity_main)

        mydb = DBHelper(this)
        empty = findViewById(R.id.empty)
        scrollView = findViewById(R.id.scrollView)
        todayContainer = findViewById(R.id.todayContainer)
        tomorrowContainer = findViewById(R.id.tomorrowContainer)
        otherContainer = findViewById(R.id.otherContainer)
        taskListToday = findViewById(R.id.taskListToday)
        taskListTomorrow = findViewById(R.id.taskListTomorrow)
        taskListUpcoming = findViewById(R.id.taskListUpcoming)
    }

    fun openAddModifyTask(view : View) {
        startActivity(Intent(this, ModifyTask::class.java))
    }

    public override fun onResume() {
        super.onResume()
        populateData()
    }

    private fun populateData() {
        mydb = DBHelper(this)
        runOnUiThread { fetchDataFromDB() }
    }

    private fun fetchDataFromDB() {

        todayList.clear()
        tomorrowList.clear()
        upcomingList.clear()

        val today: Cursor = mydb.gettodayTask()
        val tomorrow: Cursor = mydb.tomorrowTask()
        val upcoming: Cursor = mydb.upcomingTask()

        loadDataList(today, todayList)
        loadDataList(tomorrow, tomorrowList)
        loadDataList(upcoming, upcomingList)

        if (todayList.isEmpty() && tomorrowList.isEmpty() && upcomingList.isEmpty()) {
            empty.visibility = View.VISIBLE
            scrollView.visibility = View.GONE
        } else {
            empty.visibility = View.GONE
            scrollView.visibility = View.VISIBLE
            if (todayList.isEmpty()) {
                todayContainer.visibility = View.GONE
            } else {
                todayContainer.visibility = View.VISIBLE
                loadListView(taskListToday, todayList)
            }
            if (tomorrowList.isEmpty()) {
                tomorrowContainer.visibility = View.GONE
            } else {
                tomorrowContainer.visibility = View.VISIBLE
                loadListView(taskListTomorrow, tomorrowList)
            }
            if (upcomingList.isEmpty()) {
                otherContainer.visibility = View.GONE
            } else {
                otherContainer.visibility = View.VISIBLE
                loadListView(taskListUpcoming, upcomingList)
            }
        }
    }

    private fun loadDataList(cursor: Cursor?, dataList: ArrayList<HashMap<String, String>>) {
        if (cursor != null) {
            cursor.moveToFirst()
            while (cursor.isAfterLast == false) {
                val mapToday = HashMap<String, String>()
                mapToday["id"] = cursor.getString(0).toString()
                mapToday["task"] = cursor.getString(1).toString()
                mapToday["date"] = cursor.getString(2).toString()
                mapToday["status"] = cursor.getString(3).toString()
                dataList.add(mapToday)
                cursor.moveToNext()
            }
        }
    }

    private fun loadListView(listView: NoScrollListView?, dataList: ArrayList<HashMap<String, String>>) {
        val adapter = TaskAdapter(this, dataList, mydb)
        listView!!.adapter = adapter
        listView.onItemClickListener = OnItemClickListener { parent, view, position, id ->
            val i = Intent(this@MainActivity, ModifyTask::class.java)
            i.putExtra("isModify", true)
            i.putExtra("id", dataList[+position]["id"])
            startActivity(i)
        }
    }
}