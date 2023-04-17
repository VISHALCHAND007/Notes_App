package com.example.notepad_x.repository

import androidx.lifecycle.LiveData
import com.example.notepad_x.roomdb.NotesDao
import com.example.notepad_x.roomdb.NotesEntities

class NotesRepository(private val notesDao: NotesDao) {
    val allData: LiveData<List<NotesEntities>> = notesDao.getAllNotes()

    suspend fun insert(notesEntities: NotesEntities) {
        notesDao.insert(notesEntities)
    }

    suspend fun delete(title: String, note: String) {
        notesDao.delete(title , note)
    }

    suspend fun update(title: String, note: String, id: Int) {
        notesDao.update(title, note, id)
    }
    suspend fun getNoteWithId(id: Int): NotesEntities {
        return notesDao.getNoteWithId(id)
    }
}