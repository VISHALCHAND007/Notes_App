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
import org.w3c.dom.Text

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var textTv: TextView
    private lateinit var notepadTv: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        init()
    }
    private fun init() {
        initElements()
        addAnimationAndIntent()
    }

    private fun initElements() {
        textTv = findViewById(R.id.splashTv)
        notepadTv = findViewById(R.id.notepad_x)
    }

    private fun addAnimationAndIntent() {
        addAnimation(textTv)
        addAnimation(notepadTv)
        //hiding actionbar
        supportActionBar?.hide()
        Handler(Looper.myLooper()!!).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        },4000)
    }
    private fun addAnimation(textView: TextView) {
        textView.startAnimation(AnimationUtils.loadAnimation(applicationContext, R.anim.custom_animation))
        textView.animation.start()
    }
}

