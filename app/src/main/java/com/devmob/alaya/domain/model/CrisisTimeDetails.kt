package com.devmob.alaya.domain.model

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime

data class CrisisTimeDetails @RequiresApi(Build.VERSION_CODES.O) constructor(
    val startingDate: LocalDateTime = LocalDateTime.now(),
    val endDate: LocalDateTime = LocalDateTime.now(),
    val startTime: LocalDateTime = LocalDateTime.now(),
    val endTime: LocalDateTime = LocalDateTime.now(),
)
