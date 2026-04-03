package com.example.alarm.domain.entity

sealed interface Route{
    data object Back: Route
    data object Home : Route

    data object Camera : Route

    data object Folder : Route

    data object Photos : Route

    data object Map : Route

    data class PhotoDetails(
        val photoId: String
    ): Route
}