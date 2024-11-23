package com.devmob.alaya.data


data class DailyActivityBody(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val currentProgress: Int = 0,
    val maxProgress: Int = 0,
)
