package com.devmob.alaya.domain.model

import java.io.Serializable

data  class Patient (
    val email: String,
    val name: String,
    val surname: String,
    val phone: String,
    val nextSession: String? = null,
    val nextSessionDate: String? = "",
    val nextSessionTime: String? = "",
    val profileImage: String? = null,
    val hour: String = "No tiene sesiones",
) : Serializable {
    constructor() : this("", "", "", "", "","", "", null)
}
