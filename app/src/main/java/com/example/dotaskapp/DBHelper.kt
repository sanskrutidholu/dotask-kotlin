package com.example.dotaskapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.util.*


class DBHelper(context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {

    companion object {
        const val DATABASE_NAME = "DoTaskDBHelper.db"
        const val TABLE_NAME = "todo"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE  $TABLE_NAME(id INTEGER PRIMARY KEY, task TEXT, task_at DATETIME, status INTEGER)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS  $TABLE_NAME")
        onCreate(db)
    }

    fun insertTask(task: String?, task_at: String?): Boolean {

        var date: Date
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put("task", task)
        contentValues.put("task_at", task_at)
        contentValues.put("status", 0)
        db.insert(TABLE_NAME, null, contentValues)
        return true
    }

    fun updateTask(id: String, task: String?, task_at: String?): Boolean {

        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("task", task)
        contentValues.put("task_at", task_at)
        db.update(TABLE_NAME, contentValues, "id = ? ", arrayOf(id))
        return true
    }

    fun deleteTask(id: String): Boolean {

        val db = this.writableDatabase
        db.delete(TABLE_NAME, "id = ? ", arrayOf(id))
        return true
    }

    fun updateTaskStatus(id: String, status: Int?): Boolean {

        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("status", status)
        db.update(TABLE_NAME, contentValues, "id = ? ", arrayOf(id))
        return true
    }

    fun getSingleTask(id: String): Cursor {

        val db = this.readableDatabase
        return db.rawQuery("select * from $TABLE_NAME WHERE id = '$id' order by id desc", null)

    }

    fun gettodayTask() : Cursor {
        val db = this.readableDatabase
        return db.rawQuery(
            "select * from $TABLE_NAME WHERE date(task_at) = date('now', 'localtime') order by id desc",
            null
        )

    }

    fun tomorrowTask(): Cursor {
        val db = this.readableDatabase
        return db.rawQuery(
            "select * from $TABLE_NAME WHERE date(task_at) = date('now', '+1 day', 'localtime')  order by id desc",
            null
        )
    }

    fun upcomingTask(): Cursor {
        val db = this.readableDatabase
        return db.rawQuery(
            "select * from $TABLE_NAME WHERE date(task_at) > date('now', '+1 day', 'localtime') order by id desc",
            null
        )
    }



}


