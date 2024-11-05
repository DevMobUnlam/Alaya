package com.devmob.alaya.domain.model

import com.devmob.alaya.R
import java.io.Serializable
import java.time.LocalDate
import java.time.LocalTime

data  class Patient (
    val email: String,
    val name: String,
    val surname: String,
    val phone: String,
    val nextSession: String? = null,
    val nextSessionDate: String? = "",
    val nextSessionTime: String? = "",
    val image: Int = R.drawable.logounologin,
    val hour: String = "No tiene sesiones"
) : Serializable {
    constructor() : this("", "", "", "", "","", "")
}
