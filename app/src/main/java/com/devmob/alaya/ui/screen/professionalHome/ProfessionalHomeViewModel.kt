package com.devmob.alaya.ui.screen.professionalHome

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.devmob.alaya.domain.model.User
import com.devmob.alaya.domain.model.UsersProvider

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
        users = UsersProvider.users
    }

}
