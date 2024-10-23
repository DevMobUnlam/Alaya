package com.devmob.alaya.ui.screen.login

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devmob.alaya.data.FirebaseClient
import com.devmob.alaya.domain.LoginUseCase
import com.devmob.alaya.domain.model.AuthenticationResult
import com.devmob.alaya.domain.GetRoleUseCase
import com.devmob.alaya.domain.model.User
import com.devmob.alaya.domain.model.UserRole
import com.devmob.alaya.isLogged
import com.google.firebase.firestore.Source
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val getRoleUseCase: GetRoleUseCase
) : ViewModel() {

    private val auth = FirebaseClient().auth

    private val _loading = mutableStateOf(false)
    val loading: MutableState<Boolean>
        get() = _loading

    private val _navigateToPatientHome = mutableStateOf(false)
    val navigateToPatientHome: MutableState<Boolean>
        get() = _navigateToPatientHome

    private val _navigateToProfessionalHome = mutableStateOf(false)
    val navigateToProfessionalHome: MutableState<Boolean>
        get() = _navigateToProfessionalHome

    private val _showError = mutableStateOf(false)
    val showError: MutableState<Boolean>
        get() = _showError


    fun singInWithEmailAndPassword(
        email: String,
        password: String
    ) =
        viewModelScope.launch {
            _loading.value = true
            when (val result = loginUseCase(email, password)) {
                is AuthenticationResult.Error -> {
                   // _loading.value = false
                    Log.d("login", "singInWithEmailAndPassword error: ${result.t}")
                    _showError.value = true
                }

                is AuthenticationResult.Success -> {
                    Log.d("leandro", "Singin with email fue exitoso")
                    when (getRoleUseCase(email)) {
                        UserRole.PATIENT ->
                            _navigateToPatientHome.value = true

                        UserRole.PROFESSIONAL ->
                            _navigateToProfessionalHome.value = true

                        else -> _showError.value = true
                    }
                }
            }
        }


    fun checkIfUserWasLoggedIn() {
        Log.d("leandro", "viewmodel - chequeando si el user se logeó correctamente")
        _loading.value = true
        val currentUser = auth.currentUser
        var role: UserRole?

        if (currentUser != null) {
            // Llamar a la función para obtener el rol del usuario
            viewModelScope.launch {
                Log.d("leandro", "viewmodel - coroutine de chequeo de login")
                _loading.value = true
                currentUser.email?.let {
                    role = getRoleUseCase(it, Source.CACHE)
                    when (role) {
                        UserRole.PATIENT -> {
                            _navigateToPatientHome.value = true
                            _loading.value = false
                        }

                        UserRole.PROFESSIONAL -> {
                            _navigateToProfessionalHome.value = true
                            _loading.value = false
                        }

                        else -> {
                            Log.d("leandro","se fue al else del when porque el usuario no tiene rol")
                            _loading.value = false
                        }
                    }
                }
            }
        } else {
            Log.d("leandro", "el current user es null")
            _loading.value = false
        }
    }

    fun resetError() {
        _showError.value = false
    }
}
