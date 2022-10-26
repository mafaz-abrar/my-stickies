package com.mafazabrar.mystickies

import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

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

// Set up a simple factory to create the ViewModel - this is needed to supply the argument
class WordViewModelFactory(private val repository: MainRepository) : ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteViewModel::class.java)) {
            return NoteViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class.")
    }
}