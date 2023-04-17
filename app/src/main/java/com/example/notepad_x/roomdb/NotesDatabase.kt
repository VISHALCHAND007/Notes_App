package com.example.notepad_x.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [NotesEntities::class], version = 3, exportSchema = false)
abstract class NotesDatabase : RoomDatabase() {
    abstract fun notesDao(): NotesDao

    companion object {
        private var INSTANCE: NotesDatabase? = null

        fun getDatabaseInstance(mContext: Context): NotesDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance =
                    Room.databaseBuilder(
                        mContext.applicationContext,
                        NotesDatabase::class.java,
                        "notes_database"
                    ).fallbackToDestructiveMigration()
                        .build()
                INSTANCE = instance

                instance
            }
        }
    }
}