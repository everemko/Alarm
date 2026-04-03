package com.example.alarm.domain.repository

import java.time.LocalDateTime

interface PhotoFileNameRepository {

    fun create(localDateTime: LocalDateTime): String
}