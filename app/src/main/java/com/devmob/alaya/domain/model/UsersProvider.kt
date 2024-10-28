package com.devmob.alaya.domain.model

import com.devmob.alaya.R
import java.io.Serializable


data class User (
    val name: String,
    val surname: String,
    val phone: String,
    val email: String,
    val image: Int = R.drawable.logounologin,
    val hour: String = "No tiene sesiones",
    val role: UserRole,
    val invitation: Invitation? = null,
    val professional: Professional? = null,
    val patients: List<Patient>? = null,
    val containmentNetwork: List<Contact>? = null
) : Serializable {
    constructor() : this("", "", "","", 0, "", UserRole.PROFESSIONAL)
}


object UsersProvider {
val users = listOf(
    User(name = "Ana Pérez", surname ="", phone = "", email = "", image = R.drawable.ana_perez, hour = "08:30", UserRole.PATIENT),
    User(name = "Brenda Rodríguez", surname ="", phone = "", email = "", image = R.drawable.brenda_rodriguez, hour = "09:30", UserRole.PATIENT),
    User(name = "Claudia García", surname ="", phone = "", email = "", image = R.drawable.claudia_garcia, hour = "10:30", UserRole.PATIENT),
    User(name = "Ezequiel Torres", surname ="", phone = "", email = "", image = R.drawable.ezequiel_torres, hour = "11:30", UserRole.PATIENT),
    User(name = "Federico Álvarez", surname ="", phone = "", email = "", image = R.drawable.federico_alvarez, hour = "13:00", UserRole.PATIENT),
    User(name = "Lucía Sánchez", surname ="", phone = "", email = "", image = R.drawable.lucia_sanchez, hour = "15:30", UserRole.PATIENT),
    User(name = "Matías Ramírez", surname ="", phone = "", email = "", image = R.drawable.matias_ramirez, hour = "16:30", UserRole.PATIENT),
    User(name = "Mónica Fernández", surname ="", phone = "", email = "", image = R.drawable.monica_fernandez, hour = "18:00", UserRole.PATIENT),
    User(name = "Sergio Suárez", surname ="", phone = "", email = "", image = R.drawable.sergio_suarez, hour = "19:00", UserRole.PATIENT),
    User(name = "Valeria Acosta", surname ="", phone = "", email = "", image = R.drawable.valeria_acosta, hour = "20:00", UserRole.PATIENT),
)
}

