package com.mafazabrar.mystickies

import androidx.annotation.WorkerThread
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.Flow

// Access the Note DAO from this repository, which manages data sources
class MainRepository(private val noteDao: NoteDao) {

    // Synchronous get
    val allNotes: Flow<List<Note>> = noteDao.getNotes()

    // Coroutine get
    @WorkerThread
    suspend fun getNote(noteID: Int) {
        noteDao.getNote(noteID)
    }

    // Coroutine insert
    @WorkerThread
    suspend fun insert(note: Note) {
        noteDao.insert(note)
    }

    // Coroutine update
    @WorkerThread
    suspend fun update(note: Note) {
        noteDao.update(note)
    }

    // Coroutine delete
    @WorkerThread
    suspend fun delete(note: Note) {
        noteDao.delete(note)
    }

    // Coroutine delete all
    @WorkerThread
    suspend fun deleteAll() {
        noteDao.deleteAll()
    }
}