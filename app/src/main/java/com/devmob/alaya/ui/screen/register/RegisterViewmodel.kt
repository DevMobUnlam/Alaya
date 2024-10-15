package com.devmob.alaya.ui.screen.register

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devmob.alaya.domain.AddUserToFirestoreUseCase
import com.devmob.alaya.domain.RegisterNewUserUseCase
import com.devmob.alaya.domain.model.AuthenticationResult
import com.devmob.alaya.domain.model.FirebaseResult
import com.devmob.alaya.domain.model.User
import com.devmob.alaya.domain.model.UserRole
import kotlinx.coroutines.launch

class RegisterViewmodel(
    val registerNewUserUseCase: RegisterNewUserUseCase,
    val addUserToFirestoreUseCase: AddUserToFirestoreUseCase
) : ViewModel() {

    private val _loading = MutableLiveData(false)

    private val _navigateToPatientHome = mutableStateOf(false)
    val navigateToPatientHome: MutableState<Boolean>
        get() = _navigateToPatientHome

    private val _navigateToProfessionalHome = mutableStateOf(false)
    val navigateToProfessionalHome: MutableState<Boolean>
        get() = _navigateToProfessionalHome

    private val _showError = mutableStateOf(false)
    val showError: MutableState<Boolean>
        get() = _showError


    fun createUser(
        user: User,
        password: String
    ) {
        viewModelScope.launch {

            if (_loading.value == false) {
                _loading.value = true

                when (val result = registerNewUserUseCase(user.email, password)) {
                    is AuthenticationResult.Error -> {
                        _loading.value = false
                        Log.d("registerNewUser", "registerWithEmailAndPassword error: ${result.t}")
                        _showError.value = true
                    }

                    is AuthenticationResult.Success -> {
                        when (val result = addUserToFirestoreUseCase(user)) {
                            is FirebaseResult.Success -> {
                                if (user.role == UserRole.PATIENT) {
                                    _navigateToPatientHome.value = true
                                } else {
                                    _navigateToProfessionalHome.value = true
                                }
                            }

                            is FirebaseResult.Error -> {
                                _showError.value = true
                                Log.d("addUserError", "addUserToFirestore error: ${result.t} ")
                            }
                        }
                    }
                }

            }
        }

    }

    fun resetError() {
        _showError.value = false
    }
}
