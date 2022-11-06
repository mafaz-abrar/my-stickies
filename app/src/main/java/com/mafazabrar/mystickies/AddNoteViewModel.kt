package com.mafazabrar.mystickies

import android.content.Intent
import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.w3c.dom.Text

class AddNoteViewModel(private val repo: MainRepository) : ViewModel() {

    init {
        // Init block
        Log.i("ADD NOTE VIEW MODEL", "Created.")
    }

    private suspend fun incrementParentNoteChildCount(parentNoteID: Int) {
        // Init Parent Note
        var parentNote: Note? = null
        var newParentNote: Note? = null

        viewModelScope.launch {
            // Get the Parent Note
            parentNote = repo.getNote(2)
        }

        Log.i("ADD NOTE VIEW MODEL", "Retrieved Note ID: ${parentNote?.id}")


        Log.i("ADD NOTE VIEW MODEL", "Retrieved parent note with ID: ${parentNote?.id}")

        // Increment Child Count
        val oldChildCount = parentNote?.childrenCount
        oldChildCount?.let {
            newParentNote = parentNote?.copy(childrenCount = oldChildCount + 1)

            Log.i("ADD NEW VIEW MODEL", "Created new note to update parent with ID: ${newParentNote?.id} and child count: ${newParentNote?.childrenCount}")

            val note = newParentNote
            note?.let { update(note) }
        }
    }

    // Launching scope for insert coroutine
    private fun insert(note: Note) = viewModelScope.launch {
        repo.insert(note)
    }

    // Launching scope for update coroutine
    private fun update(note: Note) = viewModelScope.launch {
        repo.update(note)
    }

    // Take in string title and content,
    // and create and add a new note to the database using the strings
    private fun insertDataAsNote(noteTitle: String, noteContent: String, parentNoteID: Int) {

        if (!TextUtils.isEmpty(noteTitle)) {
            // Create a new Note using the data
            // Using the integer 0 as ID tells Room to auto-increment the ID
            val newNote = Note(0, noteTitle, noteContent, parentNoteID, 0)

            // database insert
            insert(newNote)

            Log.i("ADD NOTE VIEW MODEL", "Inserted Note: ${newNote.title}")

            Log.i("ADD NOTE VIEW MODEL", "Attempting to get Parent Note with passed ID: $parentNoteID")
        }
    }

    // Process what should happen depending on User Actions
    fun resolveInsertNote(userActions: DetailNoteUserActions,
                      noteTitle: String,
                      noteContent: String,
                      parentNoteID: Int): Int {

        when (userActions) {
            // If the save button is pressed, if there are no errors,
            // save the new note
            DetailNoteUserActions.SAVE_BUTTON_PRESSED -> {
                if (TextUtils.isEmpty(noteTitle)) {
                    Log.i("ADD NOTE VIEW MODEL", "Note Missing Title.")
                    return ActivityResultCodes.MISSING_TITLE_CODE.code
                } else {

                    insertDataAsNote(noteTitle, noteContent, parentNoteID)

                    Log.i("ADD NOTE VIEW MODEL", "Saved New Note.")

                    return ActivityResultCodes.SAVED_NEW_NOTE_CODE.code
                }
            }

            // If the discard/delete button is pressed, return to
            // the main activity
            DetailNoteUserActions.DELETE_BUTTON_PRESSED -> {
                return ActivityResultCodes.DISCARDED_NEW_NOTE_CODE.code
            }

            // If the back button is pressed, if there is no content
            // return. If some content exists, but no title, alert the user.
            // If there is content with no errors, save new note.
            DetailNoteUserActions.BACK_BUTTON_PRESSED -> {
                if (TextUtils.isEmpty(noteTitle)) {
                    if (TextUtils.isEmpty(noteContent)) {
                        Log.i("ADD NOTE VIEW MODEL", "Note Discarded.")
                        return ActivityResultCodes.DISCARDED_NEW_NOTE_CODE.code
                    }
                    else {
                        Log.i("ADD NOTE VIEW MODEL", "Note Missing Title.")
                        return ActivityResultCodes.MISSING_TITLE_CODE.code
                    }
                }

                insertDataAsNote(noteTitle, noteContent, parentNoteID)

                Log.i("ADD NOTE VIEW MODEL", "Saved New Note.")

                return ActivityResultCodes.SAVED_NEW_NOTE_CODE.code
            }
        }
    }

    // return an empty intent
    fun getReplyIntent(): Intent = Intent()
}
