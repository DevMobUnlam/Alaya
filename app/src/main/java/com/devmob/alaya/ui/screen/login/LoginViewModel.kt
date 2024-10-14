package com.devmob.alaya.ui.screen.login

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devmob.alaya.domain.LoginUseCase
import com.devmob.alaya.domain.model.AuthenticationResult
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class LoginViewModel(private val loginUseCase: LoginUseCase) : ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth
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
                    //TODO: Cambiar por role:
                    //if (result.role == "PATIENT") { //Podría ser un when en vez de if.
                    if (email == "florencia@gmail.com") {
                        _navigateToPatientHome.value = true
                    } else {
                        _navigateToProfessionalHome.value = true
                    }
                }
            }
        }

    fun resetError() {
        _showError.value = false
    }
}