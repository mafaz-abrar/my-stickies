package com.mafazabrar.mystickies

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.Flow

// Access the Note DAO from this repository, which manages data sources
class MainRepository(private val noteDao: NoteDao) {

    // Synchronous get - initialize by setting Parent Note as Root Note
    // The Root Note is the only note that can't be viewed/edited, and it
    // is the parent note for all top level notes. It has has ID -1
    var allNotes: Flow<List<NoteWithChildNotes>> = noteDao.getNoteWithChildNotes(-1)

    fun changeParentNoteForView(newParentNoteID: Int) {
        allNotes = noteDao.getNoteWithChildNotes(newParentNoteID)
    }

    // Coroutine get
    @WorkerThread
    suspend fun getNote(noteID: Int): Note {
        return noteDao.getNote(noteID)
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