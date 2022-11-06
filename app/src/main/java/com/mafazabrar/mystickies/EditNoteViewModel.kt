package com.mafazabrar.mystickies

import android.content.Intent
import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class EditNoteViewModel(private val repo: MainRepository) : ViewModel() {

    var note: Note? = null

    init {
        Log.i("EDIT NOTE VIEW MODEL", "Created.")
    }

    // Launching scope for update coroutine
    private fun update(note: Note) = viewModelScope.launch {
        repo.update(note)
    }

    // Launching scope for delete coroutine
    private fun delete(note: Note) = viewModelScope.launch {
        repo.delete(note)
    }

    // Take in string title and content and an old note,
    // update the old note with the new data in the database
    private fun updateNoteWithData(oldNote: Note, newNoteTitle: String, newNoteContent: String) {

        val newNote = oldNote.copy(title = newNoteTitle, content = newNoteContent)

        update(newNote)

        Log.i("EDIT NOTE VIEW MODEL", "Updated Note: ${newNote.title}")
    }

    // Take in an existing note and delete it from the database
    fun deleteNote(note: Note) {
        delete(note)
        Log.i("VIEW MODEL", "Deleted Note: ${note.title}")
    }

    // Process what should happen depending on User Actions
    fun resolveUpdateNote(userActions: DetailNoteUserActions,
                          noteTitle: String,
                          noteContent: String,
                          oldNote: Note)
    : Int {

        when (userActions) {
            DetailNoteUserActions.SAVE_BUTTON_PRESSED -> {
                if (TextUtils.isEmpty(noteTitle)) {
                    Log.i("EDIT NOTE VIEW MODEL", "Note Missing Title.")

                    return ActivityResultCodes.MISSING_TITLE_CODE.code

                } else {

                    updateNoteWithData(oldNote, noteTitle, noteContent)

                    Log.i("EDIT NOTE VIEW MODEL", "Updated existing Note: ${oldNote.title}")

                    return ActivityResultCodes.UPDATED_NOTE_CODE.code
                }
            }

            DetailNoteUserActions.DELETE_BUTTON_PRESSED -> {

                deleteNote(oldNote)

                Log.i("EDIT NOTE VIEW MODEL", "Deleted existing Note: ${oldNote.title}")

                return ActivityResultCodes.DELETED_NOTE_CODE.code
            }

            DetailNoteUserActions.BACK_BUTTON_PRESSED -> {
                if (TextUtils.isEmpty(noteTitle)) {
                    return ActivityResultCodes.MISSING_TITLE_CODE.code
                }

                updateNoteWithData(oldNote, noteTitle, noteContent)

                Log.i("EDIT NOTE VIEW MODEL", "Updated existing Note: ${oldNote.title}")

                return ActivityResultCodes.UPDATED_NOTE_CODE.code
            }
        }
    }

    fun getReplyIntent(): Intent = Intent()

}