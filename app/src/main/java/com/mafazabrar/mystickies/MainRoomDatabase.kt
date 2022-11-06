package com.mafazabrar.mystickies

import android.content.Context
import android.util.Log
import androidx.lifecycle.asLiveData
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Note::class], version = 1, exportSchema = false)
public abstract class MainRoomDatabase : RoomDatabase() {

    // This abstract method exposes the DAO
    abstract fun noteDao(): NoteDao

    // Companion Object implementing Singleton
    companion object {
        @Volatile
        private var INSTANCE: MainRoomDatabase? = null

        // Singleton pattern for Root Note
        private class MainRoomDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {

            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch {
                        var noteDao = database.noteDao()

                        // Check if Root Note exists at ID -1
                        val note = noteDao.getNote(-1)



                        // If not, create Root Note
                        if (note == null) {
                            val root_note = Note(-1, "", "", 0, 0)
                            noteDao.insert(root_note)
                            Log.i("DATABASE CLASS" ,"Null root - created new root.")
                        }
                        else {
                            Log.i("DATABASE CLASS" ,"Found existing root - id: ${note.id}")
                        }

                    }
                }
            }
        }

        // Create a new instance if null, else return existing instance
        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ) : MainRoomDatabase {

            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MainRoomDatabase::class.java,
                    "note_database"
                )
                    .addCallback(MainRoomDatabaseCallback(scope))
                    .build()

                // Set the newly created instance.
                INSTANCE = instance

                // Return the newly created instance.
                instance
            }
        }
    }
}
