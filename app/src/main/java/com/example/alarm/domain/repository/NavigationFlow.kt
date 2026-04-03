package com.example.alarm.domain.repository

import com.example.alarm.domain.entity.Route

interface NavigationFlow {
    fun navigate(route: Route)
}