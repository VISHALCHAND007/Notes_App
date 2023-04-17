package com.example.notepad_x.roomdb

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NotesDao {
    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(notesEntities: NotesEntities)

    @Query ("DELETE FROM notes_table WHERE note_title = :title AND note = :note")
    suspend fun delete(title: String, note: String)

    @Query ("UPDATE notes_table SET note_title = :title, note = :note WHERE id = :id")
    suspend fun update(title: String, note: String, id: Int)

    @Query ("Select * FROM notes_table ORDER BY id ASC")
    fun getAllNotes(): LiveData<List<NotesEntities>>

    @Query ("SELECT * FROM notes_table WHERE id = :id")
    suspend fun getNoteWithId(id: Int): NotesEntities
}