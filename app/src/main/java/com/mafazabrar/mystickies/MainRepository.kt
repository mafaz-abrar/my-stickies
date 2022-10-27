package com.mafazabrar.mystickies

import androidx.annotation.WorkerThread
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.Flow

// Access the Note DAO from this repository, which manages data sources
class MainRepository(private val noteDao: NoteDao) {

    // Synchronous get
    val allNotes: Flow<List<Note>> = noteDao.getNotes()

    // Coroutine insert
    @WorkerThread
    suspend fun insert(note: Note) {
        noteDao.insert(note)
    }

    // Coroutine delete
    @WorkerThread
    suspend fun deleteAll() {
        noteDao.deleteAll()
    }
}