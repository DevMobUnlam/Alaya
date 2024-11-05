package com.devmob.alaya.domain.model

data class CrisisDetails(
    val crisisTimeDetails: CrisisTimeDetails = CrisisTimeDetails(),
    val placeList: MutableList<CrisisPlace> = mutableListOf(),
    val bodySensationList: MutableList<CrisisBodySensation> = mutableListOf(),
    val toolList: MutableList<CrisisTool> = mutableListOf(),
    val emotionList: MutableList<CrisisEmotion> = mutableListOf(),
    val notes: String = "",
    val completed: Boolean = false
)
