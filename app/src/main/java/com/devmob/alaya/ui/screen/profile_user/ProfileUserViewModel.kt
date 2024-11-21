package com.devmob.alaya.ui.screen.profile_user

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devmob.alaya.domain.GetUserDataUseCase
import com.devmob.alaya.domain.model.Professional
import com.devmob.alaya.domain.model.User
import com.devmob.alaya.domain.model.UserRole
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class ProfileUserViewModel(
    private val getUserDataUseCase: GetUserDataUseCase
)  : ViewModel() {

    private val _userState = MutableStateFlow<UserState>(UserState.Loading)
    val userState: StateFlow<UserState> = _userState

    private val _profileImage = MutableStateFlow<String?>(null)
    val profileImage: StateFlow<String?> = _profileImage

    private val _phone = MutableStateFlow<String?>(null)
    val phone: StateFlow<String?> = _phone

    private val _professional = MutableStateFlow<Professional?>(null)
    val professional: StateFlow<Professional?> = _professional

    private val storageRef = FirebaseStorage.getInstance().reference
    val email = FirebaseAuth.getInstance().currentUser?.email


    init {
        loadCurrentUser()
    }

    fun loadCurrentUser() {
        val email = FirebaseAuth.getInstance().currentUser?.email
        if (email != null) {
            loadUser(email)
        } else {
            _userState.value = UserState.Error("No se pudo obtener el email del usuario")
        }
    }

    fun loadUser(email: String) {
        viewModelScope.launch {
            try {
                val user = getUserDataUseCase.getUser(email)
                if (user != null) {
                    _userState.value = UserState.Success(user)
                    _profileImage.value = user.profileImage
                    if (user.role == UserRole.PATIENT) {
                        _professional.value = user.professional
                    }
                } else {
                    _userState.value = UserState.Error("Usuario no encontrado")
                }
            } catch (e: Exception) {
                _userState.value = UserState.Error("Error al cargar usuario")
            }
        }
    }

    fun updateProfileImage(newImage: String) {
        _profileImage.value = newImage
    }

    fun uploadImageToFirebase(uri: Uri) {
        val imageRef = storageRef.child("profile_images/${UUID.randomUUID()}.jpg")
        imageRef.putFile(uri)
            .addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    _profileImage.value = downloadUri.toString()

                    if (email != null) {
                        updateProfileImageInDatabase(email, downloadUri.toString())
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.e("ProfileUserViewModel", "Error subiendo imagen: $exception")
            }
    }

    fun updateProfileImageInDatabase(email: String, imageUrl: String) {
        viewModelScope.launch {
            try {
                val result = getUserDataUseCase.updateProfileImage(email, imageUrl)
                if (result) {
                    Log.d("ProfileUserViewModel", "Imagen de perfil actualizada con éxito")
                } else {
                    Log.e("ProfileUserViewModel", "Error al actualizar imagen de perfil")
                }
            } catch (e: Exception) {
                Log.e("ProfileUserViewModel", "Error al actualizar imagen de perfil: $e")
            }
        }
    }

    fun updatePhoneNumber(phone: String) {
        viewModelScope.launch {
            try {
                val result = email?.let { getUserDataUseCase.updatePhoneNumber(it, phone) }
                if (result == true) {
                    _phone.value = phone
                    Log.d("ProfileUserViewModel", "Número de teléfono actualizado con éxito")
                } else {
                    Log.e("ProfileUserViewModel", "Error al actualizar el número de teléfono")
                }
            } catch (e: Exception) {
                Log.e("ProfileUserViewModel", "Error al actualizar el número de teléfono: $e")
            }
        }
    }
}

sealed class UserState {
    object Loading : UserState()
    data class Success(val user: User) : UserState()
    data class Error(val message: String) : UserState()
}

