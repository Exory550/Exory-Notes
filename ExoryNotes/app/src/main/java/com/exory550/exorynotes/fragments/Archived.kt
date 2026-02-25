package com.exory550.exorynotes.fragments

import com.exory550.exorynotes.R

class Archived : ExoryNotesFragment() {

    override fun getBackground() = R.drawable.archive

    override fun getObservable() = model.archivedNotes
}
