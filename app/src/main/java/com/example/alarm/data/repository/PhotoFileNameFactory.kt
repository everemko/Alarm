package com.example.alarm.data.repository

import com.example.alarm.domain.repository.PhotoFileNameRepository
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class PhotoFileNameFactory @Inject constructor() : PhotoFileNameRepository {

    private val formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")

    override fun create(localDateTime: LocalDateTime): String {
        val timestamp = localDateTime.format(formatter)
        val suffix = Random.nextInt(1000, 100000000)
        return "ALARM_${timestamp}_$suffix.jpg"
    }
}
