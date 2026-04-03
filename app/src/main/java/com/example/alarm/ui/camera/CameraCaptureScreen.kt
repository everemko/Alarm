package com.example.alarm.ui.camera

import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.alarm.R
import com.example.alarm.ui.common.ScreenToolbar
import com.example.alarm.ui.theme.AlarmTheme

@Composable
fun CameraCaptureScreen(
    modifier: Modifier = Modifier,
    isFolderSelected: Boolean,
    hasAllPermissions: Boolean,
    isSaving: Boolean,
    onCaptureClicked: (PictureCapturer) -> Unit,
    onBack: (() -> Unit)? = null,
    onOpenFolder: (() -> Unit)? = null
) {
    val screenTitle = stringResource(R.string.home_open_camera)

    if (!isFolderSelected) {
        FolderRequiredContent(
            modifier = modifier,
            title = screenTitle,
            onBack = onBack,
            onOpenFolder = onOpenFolder
        )
        return
    }

    if (!hasAllPermissions) {
        CameraPreparingContent(
            modifier = modifier,
            title = screenTitle,
            onBack = onBack
        )
        return
    }

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraController = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(CameraController.IMAGE_CAPTURE)
        }
    }

    DisposableEffect(lifecycleOwner, cameraController) {
        cameraController.bindToLifecycle(lifecycleOwner)
        onDispose { }
    }

    val pictureCapturer = remember(context, cameraController) {
        createPictureCapturer(
            context = context,
            cameraController = cameraController
        )
    }

    CameraContent(
        modifier = modifier,
        title = screenTitle,
        isSaving = isSaving,
        onCaptureClick = {
            onCaptureClicked(pictureCapturer)
        },
        onBack = onBack
    ) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { viewContext ->
                PreviewView(viewContext).apply {
                    controller = cameraController
                    scaleType = PreviewView.ScaleType.FILL_CENTER
                }
            }
        )
    }
}

@Composable
private fun CameraPreparingContent(
    modifier: Modifier = Modifier,
    title: String,
    onBack: (() -> Unit)? = null
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ScreenToolbar(
            title = title,
            onBack = onBack,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        CircularProgressIndicator()
        Text(
            text = stringResource(R.string.camera_opening_message),
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}

@Composable
private fun CameraContent(
    modifier: Modifier = Modifier,
    title: String,
    onCaptureClick: () -> Unit,
    onBack: (() -> Unit)? = null,
    isSaving: Boolean,
    cameraLayer: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        cameraLayer()

        ScreenToolbar(
            title = title,
            onBack = onBack,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        )

        Button(
            onClick = onCaptureClick,
            enabled = !isSaving,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(24.dp)
        ) {
            Text(stringResource(R.string.camera_capture_button))
        }

        if (isSaving) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = Color.White
            )
        }
    }
}

@Composable
private fun FolderRequiredContent(
    modifier: Modifier = Modifier,
    title: String,
    onBack: (() -> Unit)? = null,
    onOpenFolder: (() -> Unit)? = null
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ScreenToolbar(
            title = title,
            onBack = onBack,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text(stringResource(R.string.camera_folder_not_selected_error))
        Button(
            onClick = { onOpenFolder?.invoke() },
            enabled = onOpenFolder != null,
            modifier = Modifier.padding(top = 16.dp)
        ) { Text(stringResource(R.string.folder_pick_button)) }
    }
}

@Preview(name = "Camera Screen", showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun CameraCaptureScreenPreview() {
    AlarmTheme {
        CameraContent(
            title = stringResource(R.string.home_open_camera),
            onCaptureClick = {},
            isSaving = false,
            onBack = {}
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(stringResource(R.string.camera_preview_label), color = Color.White)
            }
        }
    }
}

@Preview(name = "Folder Required", showBackground = true)
@Composable
private fun FolderRequiredContentPreview() {
    AlarmTheme {
        FolderRequiredContent(
            title = stringResource(R.string.home_open_camera),
            onBack = {},
            onOpenFolder = {}
        )
    }
}

@Preview(name = "Camera Preparing", showBackground = true)
@Composable
private fun CameraPreparingContentPreview() {
    AlarmTheme {
        CameraPreparingContent(
            title = stringResource(R.string.home_open_camera),
            onBack = {}
        )
    }
}
