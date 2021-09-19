package com.example.myjourney.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN
import com.example.myjourney.R
import java.util.*
import android.content.Intent
import android.os.Handler
import android.os.Looper


class Activity_Splash : AppCompatActivity() {

    private val SPLASH_TIME_OUT:Long = 2000// x*1000 = x*1 sec

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        //set full screen
        window.setFlags(FLAG_FULLSCREEN, FLAG_FULLSCREEN );

        Handler(Looper.myLooper()!!).postDelayed(Runnable {
            startActivity(Intent(this@Activity_Splash, Activity_Main::class.java))
            finish()
        }, SPLASH_TIME_OUT)
    }

}