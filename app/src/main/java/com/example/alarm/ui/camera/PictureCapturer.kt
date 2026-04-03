package com.example.alarm.ui.camera

import com.example.alarm.domain.exceptions.CapturePhotoException
import java.io.OutputStream

interface PictureCapturer {

    @Throws(CapturePhotoException::class)
    suspend fun capture(stream: OutputStream)
}
