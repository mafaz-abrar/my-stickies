package com.mafazabrar.mystickies

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    // Add the ViewModel as a member variable using the viewModels delegate.
    // We use a factory to pass in the repository in the argument.
    private val viewModel: NoteViewModel by viewModels {
        NoteListViewModelFactory((application as MainApplication).repository)
    }

    // Var to hold the Adapter
    private lateinit var adapter: NotesAdapter

    // The OnClick listener for each item in the List View
    private fun launchDetailNoteActivity(note: Note) {
        // Create a new Detail Note Activity intent
        val intent = Intent(this, DetailNoteActivity::class.java)

        // Populate the intent
        intent.putExtra(IntentKeys.STATE_KEY.keyString, DetailNoteScreenStates.EDIT)
        intent.putExtra(IntentKeys.SCREEN_TITLE_KEY.keyString, resources.getString(R.string.note_edit_title))
        intent.putExtra(IntentKeys.DELETE_NOTE_BUTTON_TEXT_KEY.keyString, resources.getString(R.string.delete_note_button_text))

        intent.putExtra(IntentKeys.NOTE_ID_KEY.keyString, note.id)
        intent.putExtra(IntentKeys.NOTE_TITLE_KEY.keyString, note.title)
        intent.putExtra(IntentKeys.NOTE_CONTENT_KEY.keyString, note.content)

        // Start the Intent for Result with the Update Note Request Code
        startActivityForResult(intent, ActivityRequestCodes.UPDATE_NOTE_CODE.code)
    }

    // This function is for using sub notes.
    // Implemented later.
    private fun showNewStuff() {
        // Somehow change the list
        // submitted to the adapter.
        Log.i("MAIN ACTIVITY","New Stuff")
    }

    // OnCreate method
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Get the RecyclerView
        val notesRecyclerView = findViewById<RecyclerView>(R.id.RecyclerView_Main_NoteList)
        notesRecyclerView.layoutManager = LinearLayoutManager(this)

        // Using a List Adapter with DiffUtils
        adapter = NotesAdapter() {
            // Listener that launches the "New" Note Activity
            // for viewing and updating the Note.

            // Check if parent note was clicked,
            // submit a different list if parent
            // note clicked.
            if (1 == 1) {
                showNewStuff()
            }

            // TODO: implement above functionality

            launchDetailNoteActivity(it)
        }
        notesRecyclerView.adapter = adapter

        // Observing the allNotes variable
        viewModel.allNotes.observe(this,
            Observer { notes ->
                Log.i("MAIN ACTIVITY OBSERVER", "Data updated.")
                notes?.let {
                    adapter.submitList(it)
                }
            })

        // Delete all data for debugging
        //viewModel.deleteAllNotes()

        // Get the Floating Action Button
        val fab = findViewById<FloatingActionButton>(R.id.FAB_Main_AddNoteButton)

        // On Click, start the Detail Note Activity for result, with the Add Note
        // Request Code
        fab.setOnClickListener {
            // Create a new intent
            val intent = Intent(this, DetailNoteActivity::class.java)

            // Populate the intent
            intent.putExtra(IntentKeys.STATE_KEY.keyString, DetailNoteScreenStates.ADD)
            intent.putExtra(IntentKeys.SCREEN_TITLE_KEY.keyString, resources.getString(R.string.note_add_title))
            intent.putExtra(IntentKeys.DELETE_NOTE_BUTTON_TEXT_KEY.keyString, resources.getString(R.string.discard_note_button_text))

            // Start the Detail Note Activity for Result
            startActivityForResult(intent, ActivityRequestCodes.ADD_NOTE_CODE.code)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Get the data from the reply Intent
        val id: Int = data?.getIntExtra(IntentKeys.NOTE_ID_KEY.keyString, 0) ?: 0
        val title: String = data?.getStringExtra(IntentKeys.NOTE_TITLE_KEY.keyString) ?: ""
        val content: String = data?.getStringExtra(IntentKeys.NOTE_CONTENT_KEY.keyString) ?: ""

        Log.i("MAIN ACTIVITY RESULT", "Received Title: $title")
        Log.i("MAIN ACTIVITY RESULT", "Received Content: $content")

        // If the request and result codes are OK
        if (requestCode == ActivityRequestCodes.ADD_NOTE_CODE.code
            && resultCode == Activity.RESULT_OK) {

            // Create a new Note using the data
            // Using the integer 0 as ID tells Room to auto-increment the ID
            val newNote = Note(id, title, content, 0, 0)

            // Save the new note in the database
            viewModel.insertNote(newNote)

            // Show a Toast to confirm the action
            Toast.makeText(applicationContext, R.string.note_saved_message, Toast.LENGTH_LONG).show()
        }

        else if (requestCode == ActivityRequestCodes.UPDATE_NOTE_CODE.code
            && resultCode == Activity.RESULT_OK) {

            viewModel.updateNote(id, title, content)

            // Show a Toast to confirm the action
            Toast.makeText(applicationContext, R.string.note_saved_message, Toast.LENGTH_LONG).show()
        }
    }
}