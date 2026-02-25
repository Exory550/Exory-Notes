package com.exory550.exorynotes.room

import kotlinx.parcelize.Parcelize

@Parcelize
data class Audio(var name: String, val duration: Long, val timestamp: Long) : Attachment
