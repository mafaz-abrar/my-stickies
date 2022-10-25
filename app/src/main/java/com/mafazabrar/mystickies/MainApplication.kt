package com.mafazabrar.mystickies

import android.app.Application

// This class extends Application so that we can
// have a single, easy access instance of Repository and Database
class MainApplication : Application() {

    val database by lazy { MainRoomDatabase.getDatabase(this) }
    val repository by lazy { MainRepository(database.noteDao()) }
}