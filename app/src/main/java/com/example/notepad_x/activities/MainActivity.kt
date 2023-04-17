package com.example.notepad_x.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.FragmentTransaction
import androidx.room.Transaction
import com.example.notepad_x.R
import com.example.notepad_x.fragments.ChecklistFragment
import com.example.notepad_x.fragments.NotesFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var frameLayout: FrameLayout
    private lateinit var fragmentTransaction: FragmentTransaction

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }
    private fun init() {
        initElements()
        initTasks()
        initListeners()
    }

    private fun initElements() {
        frameLayout = findViewById(R.id.frameLayout)
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        fragmentTransaction = supportFragmentManager.beginTransaction()
    }

    private fun initTasks() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        fragmentTransaction.replace(R.id.frameLayout, NotesFragment()).commit()
    }

    private fun initListeners() {
        bottomNavigationView.setOnNavigationItemSelectedListener {
            val fragmentTrans: FragmentTransaction = supportFragmentManager.beginTransaction()
            when(it.itemId) {
                R.id.notesTab -> {
                    fragmentTrans.replace(R.id.frameLayout, NotesFragment())
                }
                R.id.checklistTab -> {
                    fragmentTrans.replace(R.id.frameLayout, ChecklistFragment())
                }
            }
            fragmentTrans.commit()
            return@setOnNavigationItemSelectedListener true
        }
    }

    @SuppressLint("InflateParams")
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val bottomSheetDialog = BottomSheetDialog(this, R.style.BottomSheetDialog)
        val view = LayoutInflater.from(this).inflate(R.layout.exit_dialog_layout, null)
        bottomSheetDialog.setContentView(view)
        bottomSheetDialog.setCancelable(true)
        handleBtnClicks(view, bottomSheetDialog)
        bottomSheetDialog.show()
    }

    private fun handleBtnClicks(view: View, bottomSheetDialog: BottomSheetDialog) {
        val yesBtn: Button = view.findViewById(R.id.Yes)
        val noBtn: Button = view.findViewById(R.id.No)

        yesBtn.setOnClickListener {
            finishAffinity()
        }
        noBtn.setOnClickListener {
            bottomSheetDialog.dismiss()
        }
    }
}