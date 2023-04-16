package com.example.notepad_x.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notepad_x.R
import com.example.notepad_x.adapters.NotesAdapter
import com.example.notepad_x.roomdb.NotesEntities
import com.example.notepad_x.viewModel.NoteViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog

class MainActivity : AppCompatActivity() {
    private lateinit var notesRv: RecyclerView
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var notesAdapter: NotesAdapter
    private lateinit var addNoteImgBtn: ImageButton
    private lateinit var viewModel: NoteViewModel;
    private lateinit var bottomSheet: BottomSheetDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    private fun init() {
        initElements()
        initTasks()
        updatingUI()
        initListeners()
    }

    private fun initTasks() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        supportActionBar?.hide()
    }

    private fun initElements() {
        notesRv = findViewById(R.id.notesRv)
        linearLayoutManager = LinearLayoutManager(this)
        notesAdapter = NotesAdapter(this)
        addNoteImgBtn = findViewById(R.id.addNoteIb)
    }

    private fun updatingUI() {
        notesRv.layoutManager = linearLayoutManager
        notesRv.adapter = notesAdapter

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(NoteViewModel::class.java)

        viewModel.allNotes.observe(this) { list ->
            list?.let {
                notesAdapter.updateList(it)
            }
        }
    }

    @SuppressLint("InflateParams")
    private fun initListeners() {
        addNoteImgBtn.setOnClickListener {
            bottomSheet = BottomSheetDialog(this, R.style.BottomSheetDialog)
            val view = LayoutInflater.from(this).inflate(R.layout.layout_add_note, null)
            bottomSheet.setContentView(view)
            bottomSheet.dismissWithAnimation = true
            bottomSheet.setCancelable(true)
            setUpFunctionality(view)
            bottomSheet.show()
        }
    }

    private fun setUpFunctionality(view: View?) {
        val titleEt: EditText? = view?.findViewById(R.id.titleEt)
        val noteEt: EditText? = view?.findViewById(R.id.noteEt)

        view?.findViewById<Button?>(R.id.addNoteBtn)?.setOnClickListener {
            val titleStr = titleEt?.text.toString()
            val noteStr = noteEt?.text.toString()

            if (titleStr.isNotEmpty() && noteStr.isNotEmpty()) {
                val note = NotesEntities(titleStr, noteStr)
                viewModel.insertNote(note)
                bottomSheet.dismiss()
            }
        }
    }

}