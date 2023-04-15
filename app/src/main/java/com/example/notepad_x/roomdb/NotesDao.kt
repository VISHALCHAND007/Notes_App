package com.example.notepad_x.roomdb

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NotesDao {
    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(notesEntities: NotesEntities)

    @Delete
    suspend fun delete(notesEntities: NotesEntities)

    @Update
    suspend fun update(notesEntities: NotesEntities)

    @Query ("Select * FROM notes_table ORDER BY id ASC")
    fun getAllNotes(): LiveData<List<NotesEntities>>
}