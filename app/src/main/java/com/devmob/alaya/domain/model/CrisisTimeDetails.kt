package com.devmob.alaya.domain.model

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.util.Calendar
import java.util.Date

data class CrisisTimeDetails (
    val startingDate: Date = Calendar.getInstance().time,
    val endDate: Date = Calendar.getInstance().time,
    val startTIme: Date = Calendar.getInstance().time,
    val endTime: Date = Calendar.getInstance().time,


)
