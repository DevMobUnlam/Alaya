package com.devmob.alaya.domain.model

import java.util.Date

data class Session(
    val sessionId: String = "",
    val professionalId: String = "",
    val patientId: String = "",
    val date: Date? = null,
    val duration: Int = 0,
    val dayOfWeek: List<DayOfWeek>? = null,
    val status: SessionStatus = SessionStatus.PENDING,
    val recurrence: Recurrence? = null,
    val time: String = "",
)

enum class SessionStatus {
    PENDING,
    CONFIRMED,
    CANCELLED
}

enum class Recurrence { NONE,
    WEEKLY,
    FORTNIGHTLY, //quincenal
    MONTHLY,
    DAILY, //no es muy intuitivo pero es para más de una vez por semana
    ONCE // para que programe solo una sesion, por ejemplo si tiene un paciente como consulta o paciente nuevo
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