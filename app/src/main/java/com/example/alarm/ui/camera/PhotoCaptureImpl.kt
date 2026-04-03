package com.example.alarm.ui.camera

import android.content.Context
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.impl.CaptureConfig
import androidx.camera.view.LifecycleCameraController
import androidx.core.content.ContextCompat
import com.example.alarm.domain.exceptions.CapturePhotoException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.io.OutputStream
import java.util.concurrent.Executors
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

fun createPictureCapturer(
    context: Context,
    cameraController: LifecycleCameraController,
): PictureCapturer {
    return PictureCapturerImpl(
        context = context,
        cameraController = cameraController
    )
}

class PictureCapturerImpl(
    private val context: Context,
    private val cameraController: LifecycleCameraController,
) : PictureCapturer {
    @Throws(CapturePhotoException::class)
    override suspend fun capture(stream: OutputStream): Unit = withContext(Dispatchers.Main) {
        capture(
            context = context,
            cameraController = cameraController,
            stream = stream
        )
    }

    @Throws(CapturePhotoException::class)
    suspend fun capture(
        context: Context,
        cameraController: LifecycleCameraController,
        stream: OutputStream
    ): Unit = suspendCancellableCoroutine { continuation ->
        val options = ImageCapture.OutputFileOptions
            .Builder(stream)
            .build()
        cameraController.imageCaptureMode = ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY
        cameraController.takePicture(
            options,
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {

                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val isClosed = runCatching { stream.close() }.isSuccess
                    if (!continuation.isActive) return

                    if (isClosed) {
                        continuation.resume(Unit)
                    } else {
                        continuation.resumeWithException(CapturePhotoException())
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    runCatching { stream.close() }
                    if (!continuation.isActive) return
                    continuation.resumeWithException(CapturePhotoException())
                }
            }
        )
    }
}
