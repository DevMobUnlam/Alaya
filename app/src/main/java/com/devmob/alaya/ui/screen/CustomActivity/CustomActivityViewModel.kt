package com.devmob.alaya.ui.screen.CustomActivity

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.devmob.alaya.domain.model.OptionTreatment

class CustomActivityViewModel : ViewModel() {
    private val _customActivities = mutableStateListOf<OptionTreatment>()
    val customActivities: List<OptionTreatment> get() = _customActivities

    fun saveActivity(activity: OptionTreatment) {
        _customActivities.add(activity)
    }
}