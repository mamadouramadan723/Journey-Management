package com.example.myjourney.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myjourney.R

class Activity_Settings : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed() //goto previous activity
        return super.onSupportNavigateUp()
    }

}