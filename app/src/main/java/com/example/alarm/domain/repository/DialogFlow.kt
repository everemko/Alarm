package com.example.alarm.domain.repository

import com.example.alarm.domain.entity.Dialog
import kotlinx.coroutines.flow.StateFlow

interface DialogFlow {
    val currentDialog: StateFlow<Dialog?>

    fun show(dialog: Dialog)

    fun dismiss()
}
