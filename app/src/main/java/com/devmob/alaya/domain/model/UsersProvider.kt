package com.devmob.alaya.domain.model

import java.io.Serializable


data class User (
    val name: String = "",
    val surname: String = "",
    val phone: String = "",
    val email: String = "",
    val profileImage: String = "",
    val hour: String = "No tiene sesiones",
    val role: UserRole = UserRole.NONE,
    val invitation: Invitation? = null,
    val invitations: List<Invitation> = emptyList(),
    val professional: Professional? = null,
    var patients: List<Patient>? = null,
    val containmentNetwork: List<Contact>? = null,
    val stepCrisis: List<OptionTreatment>? = null,
    val sessions: List<Session>? = emptyList()
) : Serializable {
    constructor() : this("", "", "", "", "","No tiene sesiones", UserRole.NONE)
    constructor(name: String, surname: String, email: String, role: UserRole) : this(name, surname, "", email, role = role)
}




