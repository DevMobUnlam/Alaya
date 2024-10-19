package com.devmob.alaya.ui.screen.crisis_registration

import com.devmob.alaya.domain.model.CrisisDetails


data class CrisisRegistrationScreenState(
    val totalSteps : Int = 6,
    val currentStep: Int = 1,
    val crisisDetails: CrisisDetails = CrisisDetails(),
    val isEditing: Boolean = false
)
