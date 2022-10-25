package com.mafazabrar.mystickies

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class NoteViewModel(private val repo: MainRepository) : ViewModel() {

    // Init block
    init {
        Log.i("NOTE VIEW MODEL", "View Model created.")
    }

    // Synchronous get
    val allNotes: LiveData<List<Note>> = repo.allNotes.asLiveData()

    // Launching scope for coroutine
    fun insert(note: Note) = viewModelScope.launch {
        repo.insert(note)
    }
}