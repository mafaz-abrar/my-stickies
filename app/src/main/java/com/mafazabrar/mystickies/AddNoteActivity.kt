package com.mafazabrar.mystickies

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider

// This Activity will store a new Note into the database,
// as added by the user.
class AddNoteActivity : AppCompatActivity() {

    // Add the ViewModel as a member variable using the viewModels delegate.
    // We use a factory to pass in the repository in the argument.
    private val viewModel: AddNoteViewModel by viewModels {
        NoteListViewModelFactory((application as MainApplication).repository)
    }

    // Var to track whether an input error occurred
    private var saveError = false

    // Resolve the processes when a User takes an action
    private fun resolveActivityForResult(userAction: DetailNoteUserActions) {
        // Get the Title and Content Views
        val noteTitleEditTextView = findViewById<EditText>(R.id.EditText_NewNoteActivity_NoteTitle)
        val noteContentEditTextView = findViewById<EditText>(R.id.EditText_NewNoteActivity_NoteContent)

        // Get the data from the Views
        val currentNoteTitle = noteTitleEditTextView.text.toString()
        val currentNoteContent = noteContentEditTextView.text.toString()

        // Get the data from the intent
        val parentNoteID = intent.getIntExtra(IntentKeys.PARENT_NOTE_ID_KEY.keyString, 0)

        // Resolve in ViewModel
        val resultCode = viewModel.resolveInsertNote(userAction, currentNoteTitle, currentNoteContent, parentNoteID)
        val replyIntent = viewModel.getReplyIntent()

        Log.i("ADD NOTE ACTIVITY", "Received result code: $resultCode")

        // Handle edge case where title is missing. Alert user and don't return to Main.
        if (resultCode == ActivityResultCodes.MISSING_TITLE_CODE.code) {
            Toast.makeText(applicationContext, R.string.note_missing_title_message, Toast.LENGTH_LONG).show()
            saveError = true
            return
        }

        setResult(resultCode, replyIntent)
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_note)

        // Get the Relevant Views
        val noteTitleEditTextView = findViewById<EditText>(R.id.EditText_NewNoteActivity_NoteTitle)
        val noteContentEditTextView = findViewById<EditText>(R.id.EditText_NewNoteActivity_NoteContent)

        val saveNoteButton = findViewById<Button>(R.id.Button_NewNoteActivity_SaveNote)
        val discardNoteButton = findViewById<Button>(R.id.Button_NewNoteActivity_DiscardNote)


        // Set Initial Data
        title = resources.getString(R.string.note_add_title)
        discardNoteButton.text = resources.getString(R.string.discard_note_button_text)
        noteTitleEditTextView.setText("")
        noteContentEditTextView.setText("")


        // On clicking the save button:
        saveNoteButton.setOnClickListener() {
            resolveActivityForResult(DetailNoteUserActions.SAVE_BUTTON_PRESSED)
        }

        // On clicking the discard button:
        discardNoteButton.setOnClickListener() {
            resolveActivityForResult(DetailNoteUserActions.DELETE_BUTTON_PRESSED)
        }
    }

    // On clicking the back button
    override fun onBackPressed() {
        resolveActivityForResult(DetailNoteUserActions.BACK_BUTTON_PRESSED)
        if (!saveError) super.onBackPressed()
    }
}