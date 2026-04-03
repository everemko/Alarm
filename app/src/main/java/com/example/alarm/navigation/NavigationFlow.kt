package com.example.alarm.navigation

import com.example.alarm.domain.entity.Route
import com.example.alarm.domain.repository.NavigationFlow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class NavigationFlowImpl @Inject constructor() : NavigationFlow {
    private val _events = MutableSharedFlow<Route>(extraBufferCapacity = 1)
    val events: SharedFlow<Route> = _events.asSharedFlow()

    override fun navigate(route: Route) {
        _events.tryEmit(route)
    }
}
