package com.example.alarm.domain.entity

import android.net.Uri
import kotlinx.serialization.Serializable

@Serializable
data class PhotoFolder(
    val uri: Uri
)
