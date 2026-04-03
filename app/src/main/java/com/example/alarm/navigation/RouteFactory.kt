package com.example.alarm.navigation

import com.example.alarm.domain.entity.Route

private const val HOME = "home"
private const val CAMERA = "camera"
private const val FOLDER = "folder"
private const val PHOTOS = "photos"
private const val MAP = "map"
private const val PHOTO_DETAILS = "photoDetails"

const val PHOTO_ID_ARG = "photoId"

object RouteFactory {
    fun home(): String = HOME

    fun camera(): String = CAMERA

    fun folder(): String = FOLDER

    fun photos(): String = PHOTOS

    fun mapScreen(): String = MAP

    fun photoDetailsPattern(): String = "$PHOTO_DETAILS/{$PHOTO_ID_ARG}"

    fun photoDetails(photoId: String): String = "$PHOTO_DETAILS/$photoId"

    fun map(route: Route): String {
        return when (route) {
            is Route.Home -> HOME
            is Route.Camera -> CAMERA
            is Route.Folder -> FOLDER
            is Route.Photos -> PHOTOS
            is Route.Map -> MAP
            is Route.PhotoDetails -> photoDetails(route.photoId)
            Route.Back -> HOME
        }
    }
}
