package com.devmob.alaya.domain.model

import com.devmob.alaya.R


data class User (val name: String, val image: Int, val hour: String)

object UsersProvider {


val users = listOf(
    User("Ana Pérez", R.drawable.ana_perez,"08:30"),
    User("Brenda Rodríguez", R.drawable.brenda_rodriguez,"09:30"),
    User("Claudia García", R.drawable.claudia_garcia,"10:30"),
    User("Ezequiel Torres", R.drawable.ezequiel_torres,"11:30"),
    User("Federico Álvarez", R.drawable.federico_alvarez,"13:00"),
    User("Lucía Sánchez", R.drawable.lucia_sanchez,"15:30"),
    User("Matías Ramírez", R.drawable.matias_ramirez,"16:30"),
    User("Mónica Fernández", R.drawable.monica_fernandez,"18:00"),
    User("Sergio Suárez", R.drawable.sergio_suarez,"19:00"),
    User("Valeria Acosta", R.drawable.valeria_acosta,"20:00"),
)
}

