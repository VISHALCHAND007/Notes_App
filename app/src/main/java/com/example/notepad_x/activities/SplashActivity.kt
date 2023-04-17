package com.example.notepad_x.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import com.example.notepad_x.R

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val textView: TextView = findViewById(R.id.splashTv)
        textView.startAnimation(AnimationUtils.loadAnimation(applicationContext, R.anim.custom_animation))
        textView.animation.start()
        //hiding actionbar
        supportActionBar?.hide()
        Handler(Looper.myLooper()!!).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        },4000)
    }
}

