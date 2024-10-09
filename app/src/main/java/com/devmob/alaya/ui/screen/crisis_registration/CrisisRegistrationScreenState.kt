package com.devmob.alaya.ui.screen.crisis_registration

import android.os.Build
import androidx.annotation.RequiresApi
import com.devmob.alaya.domain.model.CrisisBodySensation
import com.devmob.alaya.domain.model.CrisisEmotion
import com.devmob.alaya.domain.model.CrisisPlace
import com.devmob.alaya.domain.model.CrisisTool
import java.time.LocalDateTime

data class CrisisRegistrationScreenState @RequiresApi(Build.VERSION_CODES.O) constructor(
    val totalSteps : Int = 0,
    val currentStep: Int = 1,
    val startingDate: LocalDateTime = LocalDateTime.now(),
    val endDate: LocalDateTime = LocalDateTime.now(),
    val startTime: LocalDateTime = LocalDateTime.now(),
    val endTime: LocalDateTime = LocalDateTime.now(),
    val placeList: MutableList<CrisisPlace> = mutableListOf(),
    val bodySensationList: MutableList<CrisisBodySensation> = mutableListOf(),
    val toolList: MutableList<CrisisTool> = mutableListOf(),
    val emotionList: MutableList<CrisisEmotion> = mutableListOf()
)
