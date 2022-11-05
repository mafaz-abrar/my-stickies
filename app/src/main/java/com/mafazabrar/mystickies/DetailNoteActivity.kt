package com.mafazabrar.mystickies

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

// This Activity will store a new Note into the database,
// as added by the user.
class DetailNoteActivity : AppCompatActivity() {

    // View Model for business logic
    private lateinit var viewModel: DetailNoteViewModel

    private fun resolveActivityForResult() {
        // Get the Title and Content Views
        val noteTitleEditTextView = findViewById<EditText>(R.id.EditText_NewNoteActivity_NoteTitle)
        val noteContentEditTextView = findViewById<EditText>(R.id.EditText_NewNoteActivity_NoteContent)

        val noteID = intent.getIntExtra(IntentKeys.NOTE_ID_KEY.keyString, 0)
        val noteTitle = noteTitleEditTextView.text.toString()
        val noteContent = noteContentEditTextView.text.toString()

        val resultCode = viewModel.getResultCode(noteTitle)
        val replyIntent = viewModel.getReplyIntent(noteID, noteTitle, noteContent)

        setResult(resultCode, replyIntent)
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_note)

        // Set the View Model
        viewModel = ViewModelProvider(this).get(DetailNoteViewModel::class.java)


        // Get the Relevant Views
        val noteTitleEditTextView = findViewById<EditText>(R.id.EditText_NewNoteActivity_NoteTitle)
        val noteContentEditTextView = findViewById<EditText>(R.id.EditText_NewNoteActivity_NoteContent)

        val saveNoteButton = findViewById<Button>(R.id.Button_NewNoteActivity_SaveNote)
        val deleteNoteButton = findViewById<Button>(R.id.Button_NewNoteActivity_DiscardNote)


        // Get the relevant data from the intent
        intent.getSerializableExtra(IntentKeys.STATE_KEY.keyString)?.let {
            viewModel.state = it as DetailNoteScreenStates
        }
        val screenTitle = intent.getStringExtra(IntentKeys.SCREEN_TITLE_KEY.keyString)
        val deleteNoteButtonText = intent.getStringExtra(IntentKeys.DELETE_NOTE_BUTTON_TEXT_KEY.keyString)

        // Set relevant data
        title = screenTitle
        deleteNoteButton.text = deleteNoteButtonText

        when (viewModel.state) {
            DetailNoteScreenStates.ADD -> {
                noteTitleEditTextView.setText("")
                noteContentEditTextView.setText("")
            }

            DetailNoteScreenStates.EDIT -> {
                intent.getStringExtra(IntentKeys.NOTE_TITLE_KEY.keyString)?.let {
                    noteTitleEditTextView.setText(it)
                }
                intent.getStringExtra(IntentKeys.NOTE_CONTENT_KEY.keyString)?.let {
                    noteContentEditTextView.setText(it)
                }
            }
        }


        // On clicking the save button:
        saveNoteButton.setOnClickListener() {
            resolveActivityForResult()
        }
    }

    override fun onBackPressed() {
        resolveActivityForResult()
        super.onBackPressed()
    }
}