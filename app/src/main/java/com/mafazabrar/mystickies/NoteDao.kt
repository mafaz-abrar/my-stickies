package com.mafazabrar.mystickies

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    // Synchronous get
    @Query("SELECT * FROM notes")
    fun getNotes(): Flow<List<Note>>

    // Coroutine insert
    @Insert
    suspend fun insert(note: Note)

    // Coroutine delete all
    @Query("DELETE FROM notes")
    suspend fun deleteAll()
}