package com.mafazabrar.mystickies

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
    private val viewModel: NoteListViewModel by viewModels {
        NoteListViewModelFactory((application as MainApplication).repository)
    }

    // Var to hold the Adapter
    private lateinit var adapter: NotesAdapter

    // The OnClick listener for each item in the List View
    private fun launchDetailNoteActivity(note: Note) {
        // Create a new Edit Note Activity intent
        val intent = Intent(this, EditNoteActivity::class.java)

        // Populate the intent with the current selected Note
        intent.putExtra(IntentKeys.NOTE_KEY.keyString, note)

        // Start the Intent for Result with the Detail Note request code
        startActivityForResult(intent, ActivityRequestCodes.DETAIL_NOTE_CODE.code)
    }

    // OnCreate method
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Get the RecyclerView
        val notesRecyclerView = findViewById<RecyclerView>(R.id.RecyclerView_Main_NoteList)

        // Add the Layout Manager
        notesRecyclerView.layoutManager = LinearLayoutManager(this)

        // Add the adapter
        // Using a List Adapter with DiffUtils
        adapter = NotesAdapter() {
            // Listener that launches the "New" Note Activity
            // for viewing and updating the Note.

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

        // On Click, start the Add Note Activity for result, with the Floating
        // Button request code
        fab.setOnClickListener {
            val intent = Intent(this, AddNoteActivity::class.java)
            startActivityForResult(intent, ActivityRequestCodes.FLOATING_BUTTON_CODE.code)
        }
    }

    // Handle the result code and display a status message Toast to the user
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Show a status message depending on the state of the activity
        val statusMessage = viewModel.getStatusMessage(resultCode)

        Toast.makeText(applicationContext, statusMessage, Toast.LENGTH_LONG).show()
    }
}