package com.example.notepad_x.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat.finishAffinity
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

class NotesFragment : Fragment() {
    private lateinit var notesRv: RecyclerView
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var notesAdapter: NotesAdapter
    private lateinit var addNoteImgBtn: ImageButton
    private lateinit var viewModel: NoteViewModel
    private lateinit var bottomSheet: BottomSheetDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_notes, container, false)
        init(view)
        return view
    }
    private fun init(view: View) {
        initElements(view)
        initTasks()
        updatingUI()
        initListeners()
    }

    private fun initTasks() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    private fun initElements(view: View) {
        notesRv = view.findViewById(R.id.notesRv)
        linearLayoutManager = LinearLayoutManager(context)
        notesAdapter = NotesAdapter(context)
        addNoteImgBtn = view.findViewById(R.id.addNoteIb)
    }

    private fun updatingUI() {
        notesRv.layoutManager = linearLayoutManager
        notesRv.adapter = notesAdapter

        viewModel = ViewModelProvider(this)[NoteViewModel::class.java]

        viewModel.allNotes.observe(viewLifecycleOwner) { list ->
            list?.let {
                notesAdapter.updateList(it)
            }
        }
    }

    @SuppressLint("InflateParams")
    private fun openBottomSheet(buttonText: String, id: Int) {
        bottomSheet = context?.let { BottomSheetDialog(it, R.style.BottomSheetDialog) }!!
        val view = LayoutInflater.from(context).inflate(R.layout.layout_add_note, null)
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
}