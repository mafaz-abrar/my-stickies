package com.mafazabrar.mystickies

enum class ActivityResultCodes(val code: Int) {
    MISSING_TITLE_CODE(1),
    SAVED_NEW_NOTE_CODE(2),
    DISCARDED_NEW_NOTE_CODE(3),
    UPDATED_NOTE_CODE(4),
    DELETED_NOTE_CODE(5)
}