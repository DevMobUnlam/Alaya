package com.devmob.alaya.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.devmob.alaya.R
import com.devmob.alaya.ui.screen.User

class ProfessionalHomeViewModel : ViewModel() {
    var nameProfessional by mutableStateOf("")
    var users by mutableStateOf<List<User>>(emptyList())
    init {
        fetchProfessional()
        fetchUsers()
    }

    private fun fetchProfessional() {
        nameProfessional = "Patricia"
    }

    private fun fetchUsers() {
        users = listOf(
            User("Ana Pérez", R.drawable.ic_launcher_foreground,"08:30"),
            User("Brenda Rodríguez", R.drawable.ic_launcher_foreground,"09:30"),
            User("Claudia García", R.drawable.ic_launcher_foreground,"10:30"),
            User("Ezequiel Torres", R.drawable.ic_launcher_foreground,"11:30"),
            User("Federico Álvarez", R.drawable.ic_launcher_foreground,"12:30"),
            User("Lucía Sánchez", R.drawable.ic_launcher_foreground,"13:30"),
            User("Matías Ramírez", R.drawable.ic_launcher_foreground,"15:00"),
            User("Mónica Fernández", R.drawable.ic_launcher_foreground,"16:00"),
            User("Sergio Suárez", R.drawable.ic_launcher_foreground,"17:00"),
            User("Valeria Acosta", R.drawable.ic_launcher_foreground,"18:30"),
            User("Walter Quiroz", R.drawable.ic_launcher_foreground,"19:30")
        )
    }

}
