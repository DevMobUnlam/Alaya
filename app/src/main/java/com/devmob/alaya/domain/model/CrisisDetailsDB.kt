package com.devmob.alaya.domain.model

import java.time.LocalDate
import java.util.Date

data class CrisisDetailsDB(
    val start: Date?,
    val end: Date?,
    val place: String?,
    val bodySensations:List<BodySensationDB> = emptyList(),
    val tools: List<String> = emptyList(),
    val emotions: List<CrisisEmotionDB> = emptyList(),
    val notes: String?
){
    //Constructor vacio porque me da excepcion firebase
    constructor() : this(null, null, null, emptyList(), emptyList(), emptyList(), null)
}
