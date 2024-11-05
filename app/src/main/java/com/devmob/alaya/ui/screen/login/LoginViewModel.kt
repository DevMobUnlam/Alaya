package com.devmob.alaya.ui.screen.login

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devmob.alaya.data.preferences.SharedPreferences
import com.devmob.alaya.domain.LoginUseCase
import com.devmob.alaya.domain.model.AuthenticationResult
import com.devmob.alaya.domain.GetRoleUseCase
import com.devmob.alaya.domain.model.UserRole
import com.onesignal.OneSignal
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val getRoleUseCase: GetRoleUseCase,
    private val prefs: SharedPreferences
) : ViewModel() {

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
                    _loading.value = false
                    Log.d("login", "singInWithEmailAndPassword error: ${result.t}")
                    _showError.value = true
                }

                is AuthenticationResult.Success -> {
                    Log.d("login", "singInWithEmailAndPassword successful with $email")
                    OneSignal.login(email)
                    OneSignal.User.addAlias("ALIAS_FIREBASE_ID", email)
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
        val userIsLoggedIn = prefs.isLoggedIn()
        val userRole = prefs.getRole()
        Log.d("login", "user was logged in: $userIsLoggedIn")

        if (userIsLoggedIn) {
            prefs.getEmail()?.let {
                OneSignal.login(it)
                OneSignal.User.addAlias("ALIAS_FIREBASE_ID", it)
            } ?: {
                Log.w("LoginViewModel", "El usuario está logeado pero no se puede obtener el email.")
            }
            Log.d("BREN", "${OneSignal.User}")
            when (userRole) {
                UserRole.PATIENT -> {
                    _navigateToPatientHome.value = true
                }

                UserRole.PROFESSIONAL -> {
                    _navigateToProfessionalHome.value = true
                }

                else -> {
                    _loading.value = false
                }
            }
        } else {
            Log.d("login", "current user is null")
            _loading.value = false
        }
    }

    fun resetError() {
        _showError.value = false
    }
}
