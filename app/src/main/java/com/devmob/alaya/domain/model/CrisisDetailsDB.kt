package com.devmob.alaya.domain.model

import java.util.Date

data class CrisisDetailsDB(
    val start: Date?,
    val end: Date?,
    val place: String?,
    val bodySensations:List<BodySensationDB> = emptyList(),
    val tools: List<String> = emptyList(),
    val emotions: List<CrisisEmotionDB> = emptyList(),
    val notes: String?
)
