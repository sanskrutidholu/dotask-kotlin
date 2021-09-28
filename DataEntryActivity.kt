package com.example.data_viewbinding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.data_viewbinding.databinding.ActivityDataEntryBinding

class DataEntryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_entry)

        // this code is for view binding
        val binding = ActivityDataEntryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            btnSave.setOnClickListener {
                val firstName = etFirstName.text
                val secondName = etSecondName.text

                val intent = Intent(this@DataEntryActivity, MainActivity::class.java)
                intent.putExtra("first",firstName)
                intent.putExtra("second",secondName)
                startActivity(intent)
            }
        }

        //  this code is for data binding
        /*val binding : ActivityDataEntryBinding = DataBindingUtil.setContentView(this,R.layout.activity_data_entry)
        binding.apply {
            btnSave.setOnClickListener {
                val firstName = etFirstName.text
                val secondName = etSecondName.text

                val intent = Intent(this@DataEntryActivity, MainActivity::class.java)
                intent.putExtra("first",firstName)
                intent.putExtra("second",secondName)
                startActivity(intent)
            }
        }*/
    }
}