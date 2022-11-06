package com.mafazabrar.mystickies

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

// This class extends Application so that we can
// have a single, easy access instance of Repository and Database
class MainApplication : Application() {
    // No need to cancel this scope as it'll be torn down with the process
    val applicationScope = CoroutineScope(SupervisorJob())

    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    val database by lazy { MainRoomDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { MainRepository(database.noteDao()) }
}