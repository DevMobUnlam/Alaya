package com.devmob.alaya.ui.screen.send_invitation_screen

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devmob.alaya.domain.GetInvitationUseCase
import kotlinx.coroutines.launch

class SendInvitationViewModel(
    private val invitationUseCase: GetInvitationUseCase
) : ViewModel() {

    var email by mutableStateOf("")

    private val _sendInvitationStatus = MutableLiveData<Result<Unit>>()
    val sendInvitationStatus: LiveData<Result<Unit>> = _sendInvitationStatus

    fun sendInvitation(patientEmail: String, professionalEmail: String) {
        viewModelScope.launch {
            val result = invitationUseCase.sendInvitation(patientEmail, professionalEmail)
            _sendInvitationStatus.value = result
            if (result.isSuccess) {
                val r = invitationUseCase.sendNotification(patientEmail)
                if (r.isSuccessful) {
                    Log.i("SendInvitationViewModel", "Invitación enviada satisfactoriamente a: $patientEmail")
                }else{
                    Log.w("SendInvitationViewModel", "No se pudo enviar la invitación  a: $patientEmail")
                }
            }

        }
    }
}