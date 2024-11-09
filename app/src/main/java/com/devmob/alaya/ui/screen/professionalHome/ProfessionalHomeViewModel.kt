package com.devmob.alaya.ui.screen.professionalHome

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.devmob.alaya.domain.model.User
import com.devmob.alaya.domain.model.UsersProvider
import java.util.Calendar

class ProfessionalHomeViewModel : ViewModel() {
    var nameProfessional by mutableStateOf("")
    var users by mutableStateOf<List<User>>(emptyList())
    var greetingMessage by mutableStateOf("")

    init {
        fetchProfessional()
        fetchUsers()
        updateGreetingMessage()
    }

    fun fetchProfessional() {
        nameProfessional = "Patricia"
    }

    fun fetchUsers() {
        users = UsersProvider.users
    }

    fun updateGreetingMessage() {
        val calendar = Calendar.getInstance()
        val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
        greetingMessage = when (hourOfDay) {
            in 5..11 -> "Buenos días"
            in 12..19 -> "Buenas tardes"
            else -> "Buenas noches"
        }
    }

}
