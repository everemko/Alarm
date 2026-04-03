package com.example.alarm.domain.entity

sealed interface PermissionStatus {

    object Granted : PermissionStatus
    object Rejected : PermissionStatus
    object RejectedForever : PermissionStatus
}