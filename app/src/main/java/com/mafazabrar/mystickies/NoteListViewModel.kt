package com.mafazabrar.mystickies

import android.app.Activity
import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

// View Model for each Note
class NoteListViewModel(private val repo: MainRepository) : ViewModel() {

    // Init block
    init {
        Log.i("NOTE LIST VIEW MODEL", "View Model created.")
    }

    // Synchronous get the list of Notes for the current context
    val allNotes: LiveData<List<NoteWithChildNotes>> = repo.allNotes.asLiveData()

    private fun changeParentNoteForView(newParentNoteID: Int) {
        repo.changeParentNoteForView(newParentNoteID)
    }

    // Launching scope for delete all coroutine
    private fun deleteAll() = viewModelScope.launch {
        repo.deleteAll()
    }

    ////////////////////////////////
    //////// PUBLIC FUNCTIONS //////
    ////////////////////////////////

    // Delete all notes
    // For debugging purposes
    fun deleteAllNotes() {
        deleteAll()
        Log.i("VIEW MODEL", "Deleted all notes.")
    }

    // Return a status message (string resource reference) depending
    // on the result code received
    fun getStatusMessage(
        resultCode: Int,
    ): Int {

        Log.i("NOTE LIST VIEW MODEL", "Received result code: $resultCode")

        return when (resultCode) {
            ActivityResultCodes.MISSING_TITLE_CODE.code -> R.string.note_missing_title_message
            ActivityResultCodes.SAVED_NEW_NOTE_CODE.code -> R.string.note_saved_message
            ActivityResultCodes.DISCARDED_NEW_NOTE_CODE.code -> R.string.note_discarded_message
            ActivityResultCodes.UPDATED_NOTE_CODE.code -> R.string.note_saved_message
            ActivityResultCodes.DELETED_NOTE_CODE.code -> R.string.note_deleted_message
            else -> { -1 }
        }
    }
}

