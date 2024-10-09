package com.devmob.alaya.ui.screen.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth
    private val _loading = MutableLiveData(false)

    fun singInWithEmailAndPassword(email: String, password: String, homePatient: () -> Unit, homeProfessional: () -> Unit) =
        viewModelScope.launch {
            try {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("Login", "singInWithEmailAndPassword Logeuado")
                            //TODO Cambiar este if por rol del usuario
                            if (email == "e.torres.baquedano@gmail.com"){
                                homePatient()
                            } else {
                                homeProfessional()
                            }

                        } else {
                            Log.d("login", "singInWinthEmailAndPassword: ${task.result.toString()}")
                        }
                    }.addOnFailureListener {task ->
                        Log.d("login", "singInWinthEmailAndPassword: addOnFailureListener")
                        homePatient()
                    }
            } catch (ex: Exception) {
                Log.d("login", "singInWinthEmailAndPassword: ${ex.message}")
            }
        }

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
                        Log.d("Registro", "CreateWithEmailAndPassword: ${task.result.toString()}")
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