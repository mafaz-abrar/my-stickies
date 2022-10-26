package com.mafazabrar.mystickies

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

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
        adapter = NotesAdapter()
        notesRecyclerView.adapter = adapter

        // Observing the allNotes variable
        viewModel.allNotes.observe(this, Observer { notes ->
            Log.i("MAIN ACTIVITY OBSERVER", "Data updated.")
            notes?.let { adapter.submitList(it) }
        })

        val fab = findViewById<FloatingActionButton>(R.id.FAB_Main_AddNoteButton)
        fab.setOnClickListener {
            val newNote = Note(0, "Hello World!", "lorem ipsum", 0, "0 Subnotes")
            viewModel.insert(newNote)
        }
    }
}