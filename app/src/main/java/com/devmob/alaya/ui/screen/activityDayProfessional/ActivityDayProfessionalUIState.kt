package com.devmob.alaya.ui.screen.activityDayProfessional

import com.devmob.alaya.domain.model.DailyActivity

data class ActivityDayProfessionalUIState(
    val activityList: List<DailyActivity> = emptyList(),
    val isLoading: Boolean = false,
    val isCreating: Boolean = false,
    val isError: Boolean = false
)
