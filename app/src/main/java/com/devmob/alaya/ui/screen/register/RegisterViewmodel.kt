package com.devmob.alaya.ui.screen.register

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devmob.alaya.data.FirebaseClient
import com.devmob.alaya.domain.RegisterNewUserUseCase
import com.devmob.alaya.domain.model.AuthenticationResult
import com.devmob.alaya.domain.model.User
import com.devmob.alaya.domain.model.UserRole
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class RegisterViewmodel(val registerNewUserUseCase: RegisterNewUserUseCase) : ViewModel() {

    private val _loading = MutableLiveData(false)
    private val auth = FirebaseClient().auth


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
                        //createUserOnDatabase(displayName)
                        if (user.role == UserRole.PATIENT) {
                            _navigateToPatientHome.value = true
                        } else {
                            _navigateToProfessionalHome.value = true
                        }
                    }
                }
            }
        }
    }

    private fun createUserOnDatabase(newUser: User) {
        val userId = auth.currentUser?.uid
        val user = mutableMapOf<String, Any>()

        user["user_id"] = userId.toString()
        //user["display_name"] = displayName.toString()

        FirebaseFirestore.getInstance().collection("users")
            .add(user)
            .addOnSuccessListener {
                Log.d("Dato de BD", "Creado ${it.id}")
            }.addOnFailureListener {
                Log.d("Dato de BD", "Error ${it}")
            }
    }

    fun resetError() {
            _showError.value = false
    }
}