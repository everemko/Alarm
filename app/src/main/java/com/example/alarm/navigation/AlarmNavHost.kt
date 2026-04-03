package com.example.alarm.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.alarm.domain.entity.Route
import com.example.alarm.ui.camera.CameraCaptureRoute
import com.example.alarm.ui.folder.FolderPickerRoute
import com.example.alarm.ui.home.HomeRoute
import com.example.alarm.ui.map.MapRoute
import com.example.alarm.ui.photo.PhotoDetailsRoute
import com.example.alarm.ui.photos.PhotosRoute
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AlarmNavHost(
    navigationFlow: NavigationFlowImpl,
    dialogFlow: DialogFlowImpl,
) {
    val navController = rememberNavController()
    val dialog by dialogFlow.currentDialog.collectAsState()

    LaunchedEffect(navigationFlow, navController) {
        navigationFlow.events.collectLatest { route ->
            when (route) {
                Route.Back -> navController.popBackStack()
                else -> navController.navigate(RouteFactory.map(route))
            }
        }
    }

    Box {
        NavHost(navController = navController, startDestination = RouteFactory.home()) {
            composable(RouteFactory.home()) {
                HomeRoute()
            }

            composable(RouteFactory.camera()) {
                CameraCaptureRoute()
            }

            composable(RouteFactory.folder()) {
                FolderPickerRoute()
            }

            composable(RouteFactory.photos()) {
                PhotosRoute()
            }

            composable(
                route = RouteFactory.photoDetailsPattern(),
                arguments = listOf(
                    navArgument(PHOTO_ID_ARG) { type = NavType.StringType }
                )
            ) {
                PhotoDetailsRoute()
            }

            composable(RouteFactory.mapScreen()) {
                MapRoute()
            }
        }

        dialog?.let {
            AlarmDialogHost(
                dialog = it,
                onDismiss = dialogFlow::dismiss,
                onBack = { navigationFlow.navigate(Route.Back) }
            )
        }
    }
}
