package com.devmob.alaya.domain.model

import java.util.Date
import java.util.UUID

data class Session(
    val sessionId: String = UUID.randomUUID().toString(),
    val professionalId: String = "",
    val patientId: String = "",
    val date: Date? = null,
    val time: String = "",
    val status: SessionStatus = SessionStatus.PENDING
)

enum class SessionStatus {
    PENDING,
    CONFIRMED,
    CANCELLED
}

enum class DayOfWeek {
    SUNDAY,
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY
}