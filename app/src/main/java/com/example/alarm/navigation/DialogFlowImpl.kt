package com.example.alarm.navigation

import com.example.alarm.domain.entity.Dialog
import com.example.alarm.domain.repository.DialogFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DialogFlowImpl @Inject constructor() : DialogFlow {
    private val _currentDialog = MutableStateFlow<Dialog?>(null)
    override val currentDialog: StateFlow<Dialog?> = _currentDialog.asStateFlow()

    override fun show(dialog: Dialog) {
        _currentDialog.value = dialog
    }

    override fun dismiss() {
        _currentDialog.value = null
    }
}
