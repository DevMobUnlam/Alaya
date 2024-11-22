package com.devmob.alaya.ui.screen.searchUser

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.devmob.alaya.domain.GetUserDataUseCase
import com.devmob.alaya.domain.model.Patient
import kotlinx.coroutines.launch

class SearchUserViewModel (
    private val getUserData: GetUserDataUseCase
)  : ViewModel() {

    var patients by mutableStateOf<List<Patient>>(emptyList())
        private set

    fun loadPatients(professionalEmail: String) {
        viewModelScope.launch {
            val professional = getUserData.getUser(professionalEmail)
            professional?.let {
                patients = (it.patients ?: emptyList())
            }
        }
    }
}