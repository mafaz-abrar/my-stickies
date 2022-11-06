package com.mafazabrar.mystickies

import androidx.room.Embedded
import androidx.room.Relation

data class NoteWithChildNotes(
    @Embedded val parentNote: Note,
    @Relation(
        parentColumn = "id",
        entityColumn = "parent_id"
    )
    val childNotes: List<Note>
)