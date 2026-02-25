package com.exory550.exorynotes

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.exory550.exorynotes.miscellaneous.Export
import com.exory550.exorynotes.miscellaneous.IO
import com.exory550.exorynotes.miscellaneous.Operations
import com.exory550.exorynotes.preferences.AutoBackup
import com.exory550.exorynotes.preferences.Preferences
import com.exory550.exorynotes.room.Converters
import com.exory550.exorynotes.room.ExoryNotesDatabase
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.zip.ZipOutputStream

class AutoBackupWorker(private val context: Context, params: WorkerParameters) : Worker(context, params) {

    override fun doWork(): Result {
        val app = context.applicationContext as Application
        val preferences = Preferences.getInstance(app)
        val backupPath = preferences.autoBackup.value

        if (backupPath != AutoBackup.emptyPath) {
            val uri = Uri.parse(backupPath)
            val folder = requireNotNull(DocumentFile.fromTreeUri(app, uri))

            if (folder.exists()) {
                val formatter = SimpleDateFormat("yyyyMMdd HHmmss '(ExoryNotes Backup)'", Locale.ENGLISH)
                val name = formatter.format(System.currentTimeMillis())
                val file = requireNotNull(folder.createFile("application/zip", name))
                val outputStream = requireNotNull(app.contentResolver.openOutputStream(file.uri))

                val zipStream = ZipOutputStream(outputStream)

                val database = ExoryNotesDatabase.getDatabase(app)
                database.checkpoint()

                Export.backupDatabase(app, zipStream)

                val imageRoot = IO.getExternalImagesDirectory(app)
                val audioRoot = IO.getExternalAudioDirectory(app)
                database.getBaseNoteDao().getAllImages()
                    .asSequence()
                    .flatMap { string -> Converters.jsonToImages(string) }
                    .forEach { image ->
                        try {
                            Export.backupFile(zipStream, imageRoot, "Images", image.name)
                        } catch (exception: Exception) {
                            Operations.log(app, exception)
                        }
                    }
                database.getBaseNoteDao().getAllAudios()
                    .asSequence()
                    .flatMap { string -> Converters.jsonToAudios(string) }
                    .forEach { audio ->
                        try {
                            Export.backupFile(zipStream, audioRoot, "Audios", audio.name)
                        } catch (exception: Exception) {
                            Operations.log(app, exception)
                        }
                    }

                zipStream.close()
            }
        }

        return Result.success()
    }
}
