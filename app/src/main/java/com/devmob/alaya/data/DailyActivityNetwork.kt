package com.devmob.alaya.data


import java.util.Date

data class DailyActivityNetwork(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val currentProgress: Int = 0,
    val maxProgress: Int = 0,
    val lastCompleted: Date? = Date(),
) {
}