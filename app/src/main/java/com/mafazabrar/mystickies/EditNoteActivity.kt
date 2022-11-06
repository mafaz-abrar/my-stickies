package com.mafazabrar.mystickies

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels

class EditNoteActivity : AppCompatActivity() {

    // Add the ViewModel as a member variable using the viewModels delegate.
    // We use a factory to pass in the repository in the argument.
    private val viewModel: EditNoteViewModel by viewModels {
        MainViewModelFactory((application as MainApplication).repository)
    }

    // Var to track whether an input error occurred
    private var saveError = false

    private fun resolveActivityForResult(userAction: DetailNoteUserActions) {
        // Get the Title and Content Views
        val noteTitleEditTextView = findViewById<EditText>(R.id.EditText_NewNoteActivity_NoteTitle)
        val noteContentEditTextView = findViewById<EditText>(R.id.EditText_NewNoteActivity_NoteContent)

        // Get the data from the Views
        val currentNoteTitle = noteTitleEditTextView.text.toString()
        val currentNoteContent = noteContentEditTextView.text.toString()

        // Get the data from the Intent
        // If the Old Note data is missing, throw a NullReferenceException error
        val oldNote = intent.getParcelableExtra<Note>(IntentKeys.NOTE_KEY.keyString)!!

        // Resolve in ViewModel
        val resultCode = viewModel.resolveUpdateNote(userAction, currentNoteTitle, currentNoteContent, oldNote)
        val replyIntent = viewModel.getReplyIntent()

        Log.i("EDIT NOTE ACTIVITY", "Received result code: $resultCode")

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
        val deleteNoteButton = findViewById<Button>(R.id.Button_NewNoteActivity_DiscardNote)

        // Get the Note being edited from intent
        viewModel.note = intent.getParcelableExtra<Note>(IntentKeys.NOTE_KEY.keyString)

        // Set Initial Data
        title = resources.getString(R.string.note_edit_title)
        deleteNoteButton.text = resources.getString(R.string.delete_note_button_text)
        noteTitleEditTextView.setText(viewModel.note?.title)
        noteContentEditTextView.setText(viewModel.note?.content)

        // On clicking the save button:
        saveNoteButton.setOnClickListener() {
            resolveActivityForResult(DetailNoteUserActions.SAVE_BUTTON_PRESSED)
        }

        // On clicking the discard button:
        deleteNoteButton.setOnClickListener() {
            resolveActivityForResult(DetailNoteUserActions.DELETE_BUTTON_PRESSED)
        }
    }

    // On clicking the back button:
    override fun onBackPressed() {
        resolveActivityForResult(DetailNoteUserActions.BACK_BUTTON_PRESSED)
        if (!saveError) super.onBackPressed()
    }
}