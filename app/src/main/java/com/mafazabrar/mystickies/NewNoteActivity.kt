package com.mafazabrar.mystickies

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText

// This Activity will store a new Note into the database,
// as added by the user.
class NewNoteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_note)

        val noteTitleEditTextView = findViewById<EditText>(R.id.EditText_NewNoteActivity_NoteTitle)
        val noteContentEditTextView = findViewById<EditText>(R.id.EditText_NewNoteActivity_NoteContent)

        val saveNoteButton = findViewById<Button>(R.id.Button_NewNoteActivity_SaveNote)
        val discardNoteButton = findViewById<Button>(R.id.Button_NewNoteActivity_DiscardNote)

        // Listen to t
        saveNoteButton.setOnClickListener() {
            val replyIntent = Intent()

            if (TextUtils.isEmpty(noteTitleEditTextView.text)) {

                // If title not set, send error result
                setResult(RESULT_MISSING_TITLE, replyIntent)

            } else {
                // If title set:

                // Place the title and content into a reply Intent
                val title = noteTitleEditTextView.text.toString()
                val content = noteContentEditTextView.text.toString()

                replyIntent.putExtra(KEY_REPLY_NOTE_TITLE, title)
                replyIntent.putExtra(KEY_REPLY_NOTE_CONTENT, content)

                // Set the result of the Activity
                setResult(Activity.RESULT_OK, replyIntent)
            }

            finish()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

    }
}