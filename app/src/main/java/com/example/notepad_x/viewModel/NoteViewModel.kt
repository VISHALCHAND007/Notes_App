package com.example.notepad_x.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.notepad_x.repository.NotesRepository
import com.example.notepad_x.roomdb.NotesDatabase
import com.example.notepad_x.roomdb.NotesEntities
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel(application: Application): AndroidViewModel(application) {
    val allNotes: LiveData<List<NotesEntities>>
    private val repository: NotesRepository
    init {
        val dao = NotesDatabase.getDatabaseInstance(application).notesDao()
        repository = NotesRepository(dao)

        allNotes = repository.allData
    }
    fun insertNote(noteEntity: NotesEntities) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(noteEntity)
    }

    fun deleteNote(noteEntity: NotesEntities) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(noteEntity)
    }

    fun updateNote(noteEntity: NotesEntities) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(noteEntity)
    }
}