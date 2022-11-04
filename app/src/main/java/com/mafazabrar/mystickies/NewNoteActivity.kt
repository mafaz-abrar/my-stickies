package com.mafazabrar.mystickies

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText

// This Activity will store a new Note into the database,
// as added by the user.
class NewNoteActivity : AppCompatActivity() {

    private var editState = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_note)

        title = intent.getStringExtra(KEY_SCREEN_TITLE)

        val noteTitleEditTextView = findViewById<EditText>(R.id.EditText_NewNoteActivity_NoteTitle)
        val noteContentEditTextView = findViewById<EditText>(R.id.EditText_NewNoteActivity_NoteContent)

        val saveNoteButton = findViewById<Button>(R.id.Button_NewNoteActivity_SaveNote)
        val discardNoteButton = findViewById<Button>(R.id.Button_NewNoteActivity_DiscardNote)

        // If we are editing an existing Note, prefill the data
        if (title == resources.getString(R.string.note_edit_title)) {
            editState = true
            noteTitleEditTextView.setText(intent.getStringExtra(KEY_NOTE_TITLE))
            noteContentEditTextView.setText(intent.getStringExtra(KEY_NOTE_CONTENT))
        }

        // On clicking the save button:
        saveNoteButton.setOnClickListener() {
            val replyIntent = Intent()

            if (TextUtils.isEmpty(noteTitleEditTextView.text)) {

                // If title not set, send error result
                setResult(RESULT_MISSING_TITLE, replyIntent)
                Log.i("NEW NOTE ACTIVITY", "Missing Title Result Sent")

            } else {
                // If title set:

                // Place the title and content into a reply Intent
                val title = noteTitleEditTextView.text.toString()
                val content = noteContentEditTextView.text.toString()

                if (editState) {
                    val id = intent.getIntExtra(KEY_NOTE_ID, 0)
                    replyIntent.putExtra(KEY_REPLY_NOTE_ID, id)
                }

                replyIntent.putExtra(KEY_REPLY_NOTE_TITLE, title)
                replyIntent.putExtra(KEY_REPLY_NOTE_CONTENT, content)

                // Set the result of the Activity
                setResult(Activity.RESULT_OK, replyIntent)
                Log.i("NEW NOTE ACTIVITY", "OK Result Sent")
            }

            finish()
        }
    }

    override fun onBackPressed() {
        val noteTitleEditTextView = findViewById<EditText>(R.id.EditText_NewNoteActivity_NoteTitle)
        val noteContentEditTextView = findViewById<EditText>(R.id.EditText_NewNoteActivity_NoteContent)

        val replyIntent = Intent()

        if (TextUtils.isEmpty(noteTitleEditTextView.text)) {

            // If title not set, send error result
            setResult(RESULT_MISSING_TITLE, replyIntent)
            Log.i("NEW NOTE ACTIVITY", "Missing Title Result Sent")

        } else {
            // If title set:

            // Place the title and content into a reply Intent
            val title = noteTitleEditTextView.text.toString()
            val content = noteContentEditTextView.text.toString()

            if (editState) {
                val id = intent.getIntExtra(KEY_NOTE_ID, 0)
                replyIntent.putExtra(KEY_REPLY_NOTE_ID, id)
            }

            replyIntent.putExtra(KEY_REPLY_NOTE_TITLE, title)
            replyIntent.putExtra(KEY_REPLY_NOTE_CONTENT, content)

            // Set the result of the Activity
            setResult(Activity.RESULT_OK, replyIntent)
            Log.i("NEW NOTE ACTIVITY", "OK Result Sent")
        }

        finish()
        super.onBackPressed()
    }
}