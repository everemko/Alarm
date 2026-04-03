package com.example.alarm.data.system

import com.example.alarm.domain.entity.PermissionStatus
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

@Singleton
class PermissionManager @Inject constructor() {

    private data class PendingRequest(
        val permissions: List<String>,
        val continuation: CancellableContinuation<PermissionStatus>,
        val isGranted: (Map<String, Boolean>) -> Boolean
    )

    private var pendingRequest: PendingRequest? = null

    private val _requests = MutableSharedFlow<List<String>>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val requests: Flow<List<String>> = _requests.asSharedFlow()

    suspend fun requestPermissions(
        permissions: List<String>,
        isGranted: (Map<String, Boolean>) -> Boolean
    ): PermissionStatus {
        if (pendingRequest != null) return PermissionStatus.Rejected

        return suspendCancellableCoroutine { continuation ->
            pendingRequest = PendingRequest(
                permissions = permissions,
                continuation = continuation,
                isGranted = isGranted
            )

            if (!_requests.tryEmit(permissions)) {
                pendingRequest = null
                continuation.resume(PermissionStatus.Rejected)
                return@suspendCancellableCoroutine
            }

            continuation.invokeOnCancellation {
                if (pendingRequest?.continuation === continuation) {
                    pendingRequest = null
                }
            }
        }
    }

    fun onPermissionResult(
        grantResults: Map<String, Boolean>,
        shouldShowRationale: (String) -> Boolean
    ) {
        val request = pendingRequest ?: return
        pendingRequest = null

        val status = when {
            request.isGranted(grantResults) -> PermissionStatus.Granted
            request.permissions.any(shouldShowRationale) -> PermissionStatus.Rejected
            else -> PermissionStatus.RejectedForever
        }

        if (request.continuation.isActive) {
            request.continuation.resume(status)
        }
    }
}