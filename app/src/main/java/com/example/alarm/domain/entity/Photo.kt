package com.example.alarm.domain.entity

import android.net.Uri
import java.time.LocalDateTime
import java.util.UUID

data class Photo(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val uri: Uri,
    val date: LocalDateTime,
    val coordinates: Coordinates?
)
