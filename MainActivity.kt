package com.example.data_viewbinding

// for data binding we need to use layout and data in xml file
// for view binding we do not need to change xml file

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.data_viewbinding.databinding.ActivityDataEntryBinding
import com.example.data_viewbinding.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // this code is for view binding
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Here we get the data sent in the previous activity
        //And check if it is null and replace with empty string if it is
        val firstName = intent.getStringExtra("first")?:""
        val lastName = intent.getStringExtra("last")?:""

        //Using a scope function to avoid writing binding again and again
        binding.apply {
            tvFirstName.text = firstName
            tvSecondName.text = lastName
            btnBack.setOnClickListener { finish() }
        }

        // this code is for data binding
        /*val binding: ActivityMainBinding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        val firstName = intent.getStringExtra("first")?:""
        val secondName = intent.getStringExtra("second")?:""

        binding.apply {
            name = Name(firstName,secondName)
            btnBack.setOnClickListener {
                finish()
            }
        }*/

    }
}