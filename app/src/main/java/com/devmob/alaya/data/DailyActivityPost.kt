package com.devmob.alaya.data

data class DailyActivityPost(
    val title: String = "",
    val description: String = "",
    val maxProgress: Int = 0,
    val currentProgress: Int = 0,
)