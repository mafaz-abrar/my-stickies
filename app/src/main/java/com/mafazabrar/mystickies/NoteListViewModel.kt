package com.mafazabrar.mystickies

import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

// View Model for each Note
class NoteViewModel(private val repo: MainRepository) : ViewModel() {

    // Init block
    init {
        Log.i("NOTE VIEW MODEL", "View Model created.")
    }

    // Synchronous get
    val allNotes: LiveData<List<Note>> = repo.allNotes.asLiveData()

    // Launching scope for coroutine
    private fun insert(note: Note) = viewModelScope.launch {
        repo.insert(note)
    }

    // Launching scope for coroutine
    private fun update(note: Note) = viewModelScope.launch {
        repo.update(note)
    }

    // Launching scope for coroutine
    private fun deleteAll() = viewModelScope.launch {
        repo.deleteAll()
    }

    fun insertNote(note: Note) {
        insert(note)
        Log.i("VIEW MODEL","Inserted Note: ${note.title}")
    }

    fun updateNote(noteID: Int, noteTitle: String, noteContent: String) {
        val newNote = Note(noteID, noteTitle, noteContent, 0, "")
        update(newNote)
        Log.i("VIEW MODEL", "Updated Note: ${newNote.title}")
    }

    fun deleteAllNotes()
    {
        deleteAll()
        Log.i("VIEW MODEL", "Deleted all notes.")
    }
}

// Set up a simple factory to create the ViewModel - this is needed to supply the argument
class NoteListViewModelFactory(private val repository: MainRepository) : ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteViewModel::class.java)) {
            return NoteViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class.")
    }
}