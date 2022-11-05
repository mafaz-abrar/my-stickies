package com.mafazabrar.mystickies

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DetailNoteStateData(
    val state: DetailNoteScreenStates,
    val screenTitle: String,
    val deleteNoteButtonText: String,
    val note: Note?): Parcelable {
}
