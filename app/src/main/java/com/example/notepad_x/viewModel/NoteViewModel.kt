package com.example.notepad_x.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
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

    fun deleteNote(title: String, note: String) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(title , note)
    }

    fun updateNote(title: String, note: String, id: Int) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(title, note, id)
    }
    suspend fun getNoteWithId(id: Int): NotesEntities {
        return repository.getNoteWithId(id)
    }
}