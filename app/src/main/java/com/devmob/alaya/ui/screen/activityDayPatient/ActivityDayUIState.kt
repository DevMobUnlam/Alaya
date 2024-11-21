package com.devmob.alaya.ui.screen.activityDay

import com.devmob.alaya.domain.model.DailyActivity

data class ActivityDayUIState(
    val activityList: List<DailyActivity> = emptyList(),
    val isLoading: Boolean = true,
)
