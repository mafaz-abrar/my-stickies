package com.mafazabrar.mystickies

import android.app.Activity
import android.content.Intent
import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.w3c.dom.Text

class DetailNoteViewModel : ViewModel() {

    var state: DetailNoteScreenStates = DetailNoteScreenStates.ADD


    init {
        Log.i("DETAIL NOTE VIEW MODEL", "Created.")
    }

    fun getResultCode(noteTitle: String): Int {
        return if (TextUtils.isEmpty(noteTitle)) {
            Log.i("DETAIL NOTE VIEW MODEL", "Note Missing Title.")
            ActivityResultCodes.MISSING_TITLE_CODE.code
        } else {
            Log.i("DETAIL NOTE VIEW MODEL", "OK Result Set")
            Activity.RESULT_OK
        }
    }

    fun getReplyIntent(noteID: Int, noteTitle: String, noteContent: String): Intent {
        val replyIntent = Intent()

        when (state) {
            DetailNoteScreenStates.ADD -> {
                replyIntent.putExtra(IntentKeys.NOTE_ID_KEY.keyString, 0)
            }
            DetailNoteScreenStates.EDIT -> {
                replyIntent.putExtra(IntentKeys.NOTE_ID_KEY.keyString, noteID)
            }
        }

        replyIntent.putExtra(IntentKeys.NOTE_TITLE_KEY.keyString, noteTitle)
        replyIntent.putExtra(IntentKeys.NOTE_CONTENT_KEY.keyString, noteContent)

        return replyIntent
    }
}