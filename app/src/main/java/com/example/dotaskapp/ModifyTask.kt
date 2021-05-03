package com.example.dotaskapp

import android.database.Cursor
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;


class ModifyTask : AppCompatActivity() {
    lateinit var calendar: Calendar
    lateinit var mydb: DBHelper
    var isModify = false
    private lateinit var task_id: String
    private lateinit var toolbar_title: TextView
    private lateinit var edit_text: EditText
    lateinit var dateText: TextView
    private lateinit var save_btn: Button

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        setContentView(R.layout.activity_modify_task)

        mydb = DBHelper(applicationContext)
        calendar = GregorianCalendar()
        toolbar_title = findViewById(R.id.toolbar_title)
        edit_text = findViewById(R.id.edit_text)
        dateText = findViewById(R.id.dateText)
        save_btn = findViewById(R.id.save_btn)
        dateText.text = SimpleDateFormat("E, dd MMMM yyyy").format(calendar.time)

        val intent = intent
        if (intent.hasExtra("isModify")) {
            isModify = intent.getBooleanExtra("isModify", false)
            task_id = intent.getStringExtra("id").toString()
            init_modify()
        }
    }

    fun init_modify() {

        toolbar_title.text = "Modify Task"
        save_btn.text = "Update"

        val deleteTask = findViewById<LinearLayout>(R.id.deleteTask)
        deleteTask.visibility = View.VISIBLE

        val task: Cursor = mydb.getSingleTask(task_id)
        task.moveToFirst()
        edit_text.setText(task.getString(1))

        val iso8601Format = SimpleDateFormat("yyyy-MM-dd")
        try {
            calendar.time = iso8601Format.parse(task.getString(2))
        } catch (e: ParseException) {
        }
        dateText.text = SimpleDateFormat("E, dd MMMM yyyy").format(calendar.time)
    }

    fun saveTask(v : View) {

        /*Checking for Empty Task*/
        if (edit_text.text.toString().trim { it <= ' ' }.isNotEmpty()) {
            if (isModify) {
                mydb.updateTask(task_id, edit_text.text.toString(), SimpleDateFormat("yyyy-MM-dd").format(calendar.time))
                Toast.makeText(applicationContext, "Task Updated.", Toast.LENGTH_SHORT).show()
            } else {
                mydb.insertTask(edit_text.text.toString(), SimpleDateFormat("yyyy-MM-dd").format(calendar.time))
                Toast.makeText(applicationContext, "Task Added.", Toast.LENGTH_SHORT).show()
            }
            finish()
        } else {
            Toast.makeText(applicationContext, "Empty task can't be saved.", Toast.LENGTH_SHORT).show()
        }
    }

    fun deleteTask(v : View) {
        mydb.deleteTask(task_id)
        Toast.makeText(applicationContext, "Task Removed", Toast.LENGTH_SHORT).show()
        finish()
    }

    fun chooseDate(v : View) {

        val dialogView: View = View.inflate(this, R.layout.date_picker, null)

        val datePicker: DatePicker = dialogView.findViewById(R.id.date_picker)
        datePicker.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))

        val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(this)
        builder.setView(dialogView)
        builder.setTitle("Choose Date")
        builder.setNegativeButton("Cancel", null)
        builder.setPositiveButton("Set") { dialog, id ->
            calendar = GregorianCalendar(datePicker.year, datePicker.month, datePicker.dayOfMonth)
            dateText.text = SimpleDateFormat("E, dd MMMM yyyy").format(calendar.getTime())
        }
        builder.show()
    }
}