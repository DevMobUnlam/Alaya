package com.devmob.alaya.domain.model

import java.util.Calendar
import java.util.Date

data class CrisisTimeDetails(
    val startTime: Date = Calendar.getInstance().time,
    val endTime: Date = Calendar.getInstance().time,
)
