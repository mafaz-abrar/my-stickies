package com.mafazabrar.mystickies

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Note::class], version = 1, exportSchema = false)
public abstract class NoteRoomDatabase : RoomDatabase() {

    // This abstract method exposes the DAO
    abstract fun noteDao(): NoteDao

    // Companion Object implementing Singleton
    companion object {
        @Volatile
        private var INSTANCE: NoteRoomDatabase? = null

        // Create a new instance if null, else return existing instance
        fun getDatabase(context: Context) : NoteRoomDatabase {

            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NoteRoomDatabase::class.java,
                    "note_database"
                ).build()

                // Set the newly created instance.
                INSTANCE = instance

                // Return the newly created instance.
                instance
            }
        }
    }
}
