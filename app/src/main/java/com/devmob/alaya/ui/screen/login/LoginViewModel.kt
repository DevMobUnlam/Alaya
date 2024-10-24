package com.devmob.alaya.ui.screen.login

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devmob.alaya.data.preferences.SharedPreferences
import com.devmob.alaya.domain.LoginUseCase
import com.devmob.alaya.domain.model.AuthenticationResult
import com.devmob.alaya.domain.GetRoleUseCase
import com.devmob.alaya.domain.model.UserRole
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val getRoleUseCase: GetRoleUseCase,
    private val prefs: SharedPreferences
) : ViewModel() {
    private val _loading = MutableLiveData(false) //BORRAR LUEGO DE IMPLEMENTAR REGISTERVIEWMODEL

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
                    _loading.value = false
                    Log.d("login", "singInWithEmailAndPassword error: ${result.t}")
                    _showError.value = true
                }

                is AuthenticationResult.Success -> {
                    Log.d("leandro", "Singin with email fue exitoso")
                    val role = getRoleUseCase(email)
                    role?.let { prefs.setUserLoggedIn(email, it) }
                    when (role) {
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
        _loading.value = true
        Log.d("leandro", "viewmodel - chequeando si el user se logeó correctamente")
        viewModelScope.launch {
            if (prefs.isLoggedIn()) {
                when (prefs.getRole()) {
                    UserRole.PATIENT -> {
                        _navigateToPatientHome.value = true
                    }

                    UserRole.PROFESSIONAL -> {
                        _navigateToProfessionalHome.value = true
                    }

                    else -> {
                        Log.d("leandro", "se fue al else del when porque el usuario no tiene rol")
                        _loading.value = false
                    }
                }
            } else {
                Log.d("leandro", "el current user es null")
                _loading.value = false
            }
        }
    }

    fun resetError() {
        _showError.value = false
    }
}
