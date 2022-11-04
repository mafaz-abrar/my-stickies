package com.mafazabrar.mystickies

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class NotesAdapter(private val listener: (Note) -> Unit): ListAdapter<Note, NotesAdapter.NoteViewHolder>(WordsComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_note_collapsed, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class NoteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val root: ConstraintLayout = view.findViewById(R.id.ConstraintLayout_LayoutNoteCollapsed_Root)
        private val noteTitle: TextView = view.findViewById(R.id.TextView_LayoutNoteCollapsed_NoteTitle)
        private val noteData: TextView = view.findViewById(R.id.TextView_LayoutNoteCollapsed_NoteData)
        private val noteSubnotes: TextView = view.findViewById(R.id.TextView_LayoutNoteCollapsed_SubNotes)

        // This function performs the actual 'binding' of the data
        // to the ViewHolder
        fun bind(item: Note) {
            Log.i("NOTES ADAPTER", "View Holder binding item: ${item.title}")

            // Set the other values
            noteTitle.text = item.title
            noteData.text = item.content
            noteSubnotes.text = item.childrenCount.toString()

            // Set onClickListener to the listener val
            root.setOnClickListener() { listener(item) }
        }
    }

    // This utility class checks if these items are different or the same.
    class WordsComparator : DiffUtil.ItemCallback<Note>() {

        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return (
                oldItem.id == newItem.id &&
                oldItem.title == newItem.title &&
                oldItem.content == newItem.content &&
                oldItem.childrenCount == newItem.childrenCount &&
                oldItem.children == newItem.children
            )
        }
    }
}
