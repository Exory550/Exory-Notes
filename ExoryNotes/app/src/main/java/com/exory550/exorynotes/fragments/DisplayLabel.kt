package com.exory550.exorynotes.fragments

import androidx.lifecycle.LiveData
import com.exory550.exorynotes.R
import com.exory550.exorynotes.miscellaneous.Constants
import com.exory550.exorynotes.room.Item

class DisplayLabel : ExoryNotesFragment() {

    override fun getBackground() = R.drawable.label

    override fun getObservable(): LiveData<List<Item>> {
        val label = requireNotNull(requireArguments().getString(Constants.SelectedLabel))
        return model.getNotesByLabel(label)
    }
}
