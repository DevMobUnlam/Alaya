package com.devmob.alaya.utils

import java.util.Calendar
import java.util.Date

fun Date?.toCalendar(): Calendar {
    val result = Calendar.getInstance()
    this?.let {
        result.time = this
    }
    return result
}

fun Calendar?.toDate(): Date {
    return this?.time ?: Date()
}