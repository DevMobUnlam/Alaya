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

fun Date.updateDate(newDate: Date):Date {
    val newDateCalendar = newDate.toCalendar()
    val oldTimeCalendar = this.toCalendar()
    oldTimeCalendar.set(Calendar.DAY_OF_MONTH, newDateCalendar.get(Calendar.DAY_OF_MONTH))
    oldTimeCalendar.set(Calendar.MONTH, newDateCalendar.get(Calendar.MONTH))
    oldTimeCalendar.set(Calendar.YEAR, newDateCalendar.get(Calendar.YEAR))
    return oldTimeCalendar.toDate()
}

fun Date.updateHour(newDate: Date):Date {
    val newDateCalendar = newDate.toCalendar()
    val oldTimeCalendar = this.toCalendar()
    oldTimeCalendar.set(Calendar.HOUR_OF_DAY, newDateCalendar.get(Calendar.HOUR_OF_DAY))
    oldTimeCalendar.set(Calendar.MINUTE, newDateCalendar.get(Calendar.MINUTE))
    return oldTimeCalendar.toDate()
}