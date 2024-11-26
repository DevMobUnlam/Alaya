package com.devmob.alaya.domain.model


data class DailyActivity(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val currentProgress: Int = 0,
    val maxProgress: Int = 0,
    val isDone: Boolean = false
)