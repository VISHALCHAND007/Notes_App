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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notepad_x.R
import com.example.notepad_x.adapters.NotesAdapter
import com.example.notepad_x.roomdb.NotesEntities
import com.example.notepad_x.viewModel.NoteViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var notesRv: RecyclerView
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var notesAdapter: NotesAdapter
    private lateinit var addNoteImgBtn: ImageButton
    private lateinit var viewModel: NoteViewModel
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
        )[NoteViewModel::class.java]

        viewModel.allNotes.observe(this) { list ->
            list?.let {
                notesAdapter.updateList(it)
            }
        }
    }

    @SuppressLint("InflateParams")
    private fun openBottomSheet(buttonText: String, id: Int) {
        bottomSheet = BottomSheetDialog(this, R.style.BottomSheetDialog)
        val view = LayoutInflater.from(this).inflate(R.layout.layout_add_note, null)
        bottomSheet.setContentView(view)
        bottomSheet.dismissWithAnimation = true
        bottomSheet.setCancelable(true)
        setUpFunctionality(view, buttonText, id)
        bottomSheet.show()
    }

    @SuppressLint("InflateParams")
    private fun initListeners() {
        addNoteImgBtn.setOnClickListener {
            val id = -1
            openBottomSheet("Add Note", id)
        }
        //swipe listener for deleting and updating the notes
        val itemTouchHelper = ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                if (direction == ItemTouchHelper.LEFT) {
                    val note = notesAdapter.getNote(viewHolder.adapterPosition)
                    viewModel.deleteNote(note.title, note.note)
                } else {
                    val id = notesAdapter.allNotes[viewHolder.adapterPosition].id
                    openBottomSheet("Update Note", id)
                }
            }
        })
        itemTouchHelper.attachToRecyclerView(notesRv)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setUpFunctionality(view: View?, buttonText: String, id: Int) {
        val titleEt: EditText? = view?.findViewById(R.id.titleEt)
        val noteEt: EditText? = view?.findViewById(R.id.noteEt)
        val button: Button? = view?.findViewById(R.id.addNoteBtn)
        button?.text = buttonText

        if (!bottomSheet.isShowing) {
            notesAdapter.notifyDataSetChanged()
        }
        if(buttonText.contains("Update")) {
            var note: NotesEntities

            //accessing something using coroutines
            lifecycleScope.launch {
                note = viewModel.getNoteWithId(id)
                titleEt?.setText(note.title)
                noteEt?.setText(note.note)
            }
        }

            button?.setOnClickListener {
                val titleStr = titleEt?.text.toString()
                val noteStr = noteEt?.text.toString()

                if (titleStr.isNotEmpty() && noteStr.isNotEmpty()) {
                    val note = NotesEntities(titleStr, noteStr)
                    if (buttonText.contains("Add")) {
                        viewModel.insertNote(note)
                        bottomSheet.dismiss()
                    } else {
                        viewModel.updateNote(note.title, note.note, id)
                        bottomSheet.dismiss()
                    }
                }
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