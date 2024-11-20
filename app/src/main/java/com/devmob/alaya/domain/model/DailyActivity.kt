package com.devmob.alaya.domain.model

data class DailyActivity(
    val title: String = "",
    val description: String = "",
    val currentProgress: Int = 0,
    val maxProgress: Int = 0,
    val isChecked: Boolean = false
)
