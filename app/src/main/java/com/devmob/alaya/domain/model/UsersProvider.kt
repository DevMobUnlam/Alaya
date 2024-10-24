package com.devmob.alaya.domain.model

import com.devmob.alaya.R
import java.io.Serializable


data class User (
    val name: String,
    val surname: String,
    val email: String,
    val image: Int = R.drawable.logounologin,
    val hour: String = "No tiene sesiones",
    val role: UserRole,
    val containmentNetwork: List<Contact>? = null
) : Serializable {
    constructor() : this("", "", "", 0, "", UserRole.PROFESSIONAL)
}


object UsersProvider {
val users = listOf(
    User(name = "Ana Pérez", surname ="", email = "", image = R.drawable.ana_perez, hour = "08:30", UserRole.PATIENT),
    User(name = "Brenda Rodríguez", surname ="", email = "", image = R.drawable.brenda_rodriguez, hour = "09:30", UserRole.PATIENT),
    User(name = "Claudia García", surname ="", email = "", image = R.drawable.claudia_garcia, hour = "10:30", UserRole.PATIENT),
    User(name = "Ezequiel Torres", surname ="", email = "", image = R.drawable.ezequiel_torres, hour = "11:30", UserRole.PATIENT),
    User(name = "Federico Álvarez", surname ="", email = "", image = R.drawable.federico_alvarez, hour = "13:00", UserRole.PATIENT),
    User(name = "Lucía Sánchez", surname ="", email = "", image = R.drawable.lucia_sanchez, hour = "15:30", UserRole.PATIENT),
    User(name = "Matías Ramírez", surname ="", email = "", image = R.drawable.matias_ramirez, hour = "16:30", UserRole.PATIENT),
    User(name = "Mónica Fernández", surname ="", email = "", image = R.drawable.monica_fernandez, hour = "18:00", UserRole.PATIENT),
    User(name = "Sergio Suárez", surname ="", email = "", image = R.drawable.sergio_suarez, hour = "19:00", UserRole.PATIENT),
    User(name = "Valeria Acosta", surname ="", email = "", image = R.drawable.valeria_acosta, hour = "20:00", UserRole.PATIENT),
)
}

