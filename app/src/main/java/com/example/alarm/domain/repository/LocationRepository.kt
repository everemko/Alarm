package com.example.alarm.domain.repository

import com.example.alarm.domain.entity.Coordinates
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    fun observeLocation(): Flow<Coordinates>
}
