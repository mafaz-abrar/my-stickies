package com.mafazabrar.mystickies

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ActionMode
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

const val REQUEST_CODE_NEW_NOTE = 1

const val RESULT_MISSING_TITLE = 1

const val KEY_REPLY_NOTE_TITLE = "KEY_REPLY_NOTE_TITLE"
const val KEY_REPLY_NOTE_CONTENT = "KEY_REPLY_NOTE_CONTENT"

class MainActivity : AppCompatActivity() {

    // Var to hold the Adapter
    private lateinit var adapter: NotesAdapter

    // Add the ViewModel as a member variable using the viewModels delegate
    // We use a factory to pass in the argument repository
    private val viewModel: NoteViewModel by viewModels {
        WordViewModelFactory((application as MainApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Get the RecyclerView
        val notesRecyclerView = findViewById<RecyclerView>(R.id.RecyclerView_Main_NoteList)
        notesRecyclerView.layoutManager = LinearLayoutManager(this)

        // Using a List Adapter with DiffUtils
        adapter = NotesAdapter()
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
        viewModel.deleteAll()

        // Get the Floating Action Button
        val fab = findViewById<FloatingActionButton>(R.id.FAB_Main_AddNoteButton)

        // On Click, start the New Note Activity for result
        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, NewNoteActivity::class.java)

            startActivityForResult(intent, REQUEST_CODE_NEW_NOTE)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // If the request and result codes are OK
        if (requestCode == REQUEST_CODE_NEW_NOTE && resultCode == Activity.RESULT_OK) {

            // Get the data from the reply Intent
            val title: String = data?.getStringExtra(KEY_REPLY_NOTE_TITLE) ?: ""
            val content: String = data?.getStringExtra(KEY_REPLY_NOTE_CONTENT) ?: ""

            Log.i("MAIN ACTIVITY RESULT", "Received Title: $title")
            Log.i("MAIN ACTIVITY RESULT", "Received Content: $content")

            // Create a new Note using the data
            // Using the integer 0 as ID tells Room to auto-increment the ID
            val newNote = Note(0, title, content, 0, "a")

            // Save the new note in the database
            viewModel.insert(newNote)

            // Show a Toast to confirm the action
            Toast.makeText(applicationContext, R.string.note_saved_message, Toast.LENGTH_LONG).show()
        }
    }
}