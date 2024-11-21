package com.devmob.alaya.domain.model


data class DailyActivity(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val currentProgress: Int = 0,
    val maxProgress: Int = 1,
    val isDone: Boolean = false
)/* {
    constructor() : this(
        title = "",
        description = "",
        currentProgress = 0,
        maxProgress = 1,
        isDone = false
    )
}*/