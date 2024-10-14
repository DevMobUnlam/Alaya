package com.devmob.alaya.ui.screen.crisis_registration

import android.os.Build
import androidx.annotation.RequiresApi
import com.devmob.alaya.domain.model.CrisisDetails


data class CrisisRegistrationScreenState @RequiresApi(Build.VERSION_CODES.O) constructor(
    val totalSteps : Int = 6,
    val currentStep: Int = 1,
    val crisisDetails: CrisisDetails = CrisisDetails(),
    val isEditing: Boolean = false
)
