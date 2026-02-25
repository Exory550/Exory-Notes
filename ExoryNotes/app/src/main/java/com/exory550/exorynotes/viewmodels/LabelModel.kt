package com.exory550.exorynotes.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.exory550.exorynotes.room.Label
import com.exory550.exorynotes.room.ExoryNotesDatabase

class LabelModel(app: Application) : AndroidViewModel(app) {

    private val database = ExoryNotesDatabase.getDatabase(app)
    private val labelDao = database.getLabelDao()
    val labels = labelDao.getAll()

    fun insertLabel(label: Label, onComplete: (success: Boolean) -> Unit) =
        executeAsyncWithCallback({ labelDao.insert(label) }, onComplete)
}
