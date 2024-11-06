package com.devmob.alaya.domain.model


import com.google.firebase.firestore.PropertyName
import java.time.LocalDate
import java.util.Calendar
import java.util.Date

data class CrisisDetailsDB(
    val start: Date? = Calendar.getInstance().time,
    val end: Date? = Calendar.getInstance().time,
    val place: String? = "",
    val bodySensations:List<BodySensationDB> = emptyList(),
    val tools: List<String> = emptyList(),
    val emotions: List<CrisisEmotionDB> = emptyList(),
    val notes: String?,
    var completed : Boolean = false
){
    //Constructor vacio porque me da excepcion firebase
    constructor() : this(null, null, null, emptyList(), emptyList(), emptyList(), null, false)
}
