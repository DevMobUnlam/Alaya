package com.devmob.alaya.ui.screen.login

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devmob.alaya.domain.LoginUseCase
import com.devmob.alaya.domain.model.LoginResult
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

    fun singInWithEmailAndPassword(
        email: String,
        password: String
    ) =
        viewModelScope.launch {
            _loading.value = true
            when (val result = loginUseCase(email, password)) {
                is LoginResult.Error -> {
                    _loading.value = false
                    Log.d("login", "singInWithEmailAndPassword error: ${result.t}")
                    //TODO: manejo de errores
                }

                is LoginResult.Success -> {
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

    /////////////////////// BORRAR ESTO LUEGO DE IMPLEMENTAR EL OTRO VIEWMODEL


    fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        home: () -> Unit
    ) {
        if (_loading.value == false) {
            _loading.value = true
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val displayName =
                            task.result.user?.email?.split("@")?.get(0)
                        createUser(displayName)
                        home()
                    } else {
                        Log.d("Registro", "CreateWithEmailAndPassword: ${task.exception}")
                    }
                    _loading.value = false
                }
        }
    }

    private fun createUser(displayName: String?) {
        val userId = auth.currentUser?.uid
        val user = mutableMapOf<String, Any>()

        user["user_id"] = userId.toString()
        user["display_name"] = displayName.toString()

        FirebaseFirestore.getInstance().collection("users")
            .add(user)
            .addOnSuccessListener {
                Log.d("Dato de BD", "Creado ${it.id}")
            }.addOnFailureListener {
                Log.d("Dato de BD", "Error ${it}")
            }
    }




}