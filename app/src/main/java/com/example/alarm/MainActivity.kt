package com.example.alarm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.alarm.navigation.AlarmNavHost
import com.example.alarm.navigation.DialogFlowImpl
import com.example.alarm.navigation.NavigationFlowImpl
import com.example.alarm.data.system.PermissionRequestLauncher
import com.example.alarm.ui.theme.AlarmTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var navigationFlow: NavigationFlowImpl

    @Inject
    lateinit var dialogFlow: DialogFlowImpl

    @Inject
    lateinit var permissionRequestLauncher: PermissionRequestLauncher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            AlarmTheme {
                AlarmNavHost(
                    navigationFlow = navigationFlow,
                    dialogFlow = dialogFlow
                )
            }
        }
    }
}
