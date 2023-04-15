package com.example.notepad_x.repository

import androidx.lifecycle.LiveData
import com.example.notepad_x.roomdb.NotesDao
import com.example.notepad_x.roomdb.NotesEntities

class NotesRepository(private val notesDao: NotesDao) {
    val allData: LiveData<List<NotesEntities>> = notesDao.getAllNotes()

    suspend fun insert(notesEntities: NotesEntities) {
        notesDao.insert(notesEntities)
    }

    suspend fun delete(notesEntities: NotesEntities) {
        notesDao.delete(notesEntities)
    }

    suspend fun update(notesEntities: NotesEntities) {
        notesDao.update(notesEntities)
    }
}