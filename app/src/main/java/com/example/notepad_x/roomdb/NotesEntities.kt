package com.example.notepad_x.roomdb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "notes_table")
class NotesEntities(@ColumnInfo (name = "note_title") val title: String,@ColumnInfo (name = "note") val note: String) {
    @PrimaryKey (autoGenerate = true) var id: Int = 0
}