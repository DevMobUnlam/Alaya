package com.devmob.alaya.domain.model

data class CrisisRegisterData(
    var startDate : String = "",
    var endDate: String = "",
    var startTime: String = "",
    var endTime:String = "",
    var place: String = "",
    var sensations: List<CrisisBodySensation> = emptyList(),
    var emotions: List <CrisisEmotion> = emptyList(),
    var notes: String = ""
)
