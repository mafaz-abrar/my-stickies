package com.mafazabrar.mystickies

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    // Synchronous get list of child notes
    // whose parent note has ID noteID
    @Transaction
    @Query("SELECT * FROM notes WHERE id = :noteID")
    fun getNoteWithChildNotes(noteID: Int): Flow<List<NoteWithChildNotes>>

    // Synchronous get list of notes whose id is
    // noteID
    @Query("SELECT * FROM notes WHERE id = :noteID")
    suspend fun getNote(noteID: Int): Note

    //@Query("SELECT * FROM notes WHERE id = :noteID")
    //fun getNoteSync()

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