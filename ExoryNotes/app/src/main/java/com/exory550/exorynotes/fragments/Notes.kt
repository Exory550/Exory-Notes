package com.exory550.exorynotes.fragments

import android.view.Menu
import android.view.MenuInflater
import androidx.navigation.fragment.findNavController
import com.exory550.exorynotes.R
import com.exory550.exorynotes.miscellaneous.add

class Notes : ExoryNotesFragment() {

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.add(R.string.search, R.drawable.search) { findNavController().navigate(R.id.NotesToSearch) }
    }

    override fun getObservable() = model.baseNotes

    override fun getBackground() = R.drawable.notebook
}
