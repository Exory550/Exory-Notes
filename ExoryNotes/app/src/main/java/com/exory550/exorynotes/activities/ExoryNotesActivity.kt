package com.exory550.exorynotes.activities

import android.Manifest
import android.app.Activity
import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.Spannable
import android.text.style.BackgroundColorSpan
import android.util.TypedValue
import android.view.KeyEvent
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup.LayoutParams
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.getSpans
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Transition
import androidx.transition.TransitionManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.transition.MaterialFade
import com.exory550.exorynotes.R
import com.exory550.exorynotes.databinding.ActivityExoryNotesBinding
import com.exory550.exorynotes.databinding.DialogProgressBinding
import com.exory550.exorynotes.databinding.DialogReminderBinding
import com.exory550.exorynotes.image.ImageError
import com.exory550.exorynotes.miscellaneous.Constants
import com.exory550.exorynotes.miscellaneous.Operations
import com.exory550.exorynotes.miscellaneous.add
import com.exory550.exorynotes.preferences.TextSize
import com.exory550.exorynotes.recyclerview.adapter.AudioAdapter
import com.exory550.exorynotes.recyclerview.adapter.ErrorAdapter
import com.exory550.exorynotes.recyclerview.adapter.PreviewImageAdapter
import com.exory550.exorynotes.room.Audio
import com.exory550.exorynotes.room.Folder
import com.exory550.exorynotes.room.Frequency
import com.exory550.exorynotes.room.Image
import com.exory550.exorynotes.room.Reminder
import com.exory550.exorynotes.room.Type
import com.exory550.exorynotes.viewmodels.ExoryNotesModel
import com.exory550.exorynotes.widget.WidgetProvider
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.util.Calendar

abstract class ExoryNotesActivity(private val type: Type) : AppCompatActivity() {

    internal lateinit var binding: ActivityExoryNotesBinding
    internal val model: ExoryNotesModel by viewModels()

    override fun finish() {
        lifecycleScope.launch {
            model.saveNote()
            WidgetProvider.sendBroadcast(application, model.id)
            super.finish()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong("id", model.id)
        lifecycleScope.launch {
            model.saveNote()
            WidgetProvider.sendBroadcast(application, model.id)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model.type = type
        initialiseBinding()
        setContentView(binding.root)

        lifecycleScope.launch {
            if (model.isFirstInstance) {
                val persistedId = savedInstanceState?.getLong("id")
                val selectedId = intent.getLongExtra(Constants.SelectedBaseNote, 0L)
                val id = persistedId ?: selectedId
                model.setState(id)

                if (model.isNewNote && intent.action == Intent.ACTION_SEND) {
                    handleSharedNote()
                }

                model.isFirstInstance = false
            }

            setupToolbar()
            setupListeners()
            setStateFromModel()

            configureUI()
            binding.ScrollView.visibility = View.VISIBLE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_ADD_IMAGES -> {
                    val uri = data?.data
                    val clipData = data?.clipData
                    if (uri != null) {
                        val uris = arrayOf(uri)
                        model.addImages(uris)
                    } else if (clipData != null) {
                        val uris = Array(clipData.itemCount) { index -> clipData.getItemAt(index).uri }
                        model.addImages(uris)
                    }
                }
            }
        }
    }

    companion object {
        private const val REQUEST_ADD_IMAGES = 1001
    }
}
