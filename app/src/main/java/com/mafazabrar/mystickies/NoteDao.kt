package com.mafazabrar.mystickies

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    // Synchronous get
    @Query("SELECT * FROM notes")
    fun getNotes(): Flow<List<Note>>


    @Query("SELECT * FROM notes WHERE id = :noteID")
    suspend fun getNote(noteID: Int): List<Note>

    // Coroutine insert
    @Insert
    suspend fun insert(note: Note)

    // Coroutine update
    @Update
    suspend fun update(note: Note)

    // Coroutine delete
    @Delete
    suspend fun delete(note: Note)

    // Coroutine delete all
    @Query("DELETE FROM notes")
    suspend fun deleteAll()
}