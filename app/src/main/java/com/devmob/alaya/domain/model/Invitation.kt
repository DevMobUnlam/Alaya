package com.devmob.alaya.domain.model

import java.io.Serializable

data class Invitation(val professionalEmail: String, val patientEmail: String, val status: String) :
    Serializable {
    constructor() : this("", "", "")
}
