package com.devmob.alaya.ui.screen.ContainmentNetwork.Contact

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ContactViewModel : ViewModel() {

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

     fun formatPhoneNumberForWhatsApp(phoneNumber: String): String {
        val cleanedNumber = phoneNumber.replace(Regex("[^\\d]"), "")
        if (cleanedNumber.startsWith("549")) {
            return cleanedNumber
        }
        return if (cleanedNumber.startsWith("11") || cleanedNumber.length == 10) {
            "+54 9 $cleanedNumber"
        } else {
            "+54 $cleanedNumber"
        }
    }

    fun sendWhatsAppMessage(context: Context, phoneNumber: String, message: String) {
        val uri = Uri.parse("https://wa.me/$phoneNumber?text=${Uri.encode(message)}")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.setPackage("com.whatsapp")
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        } else {
            _errorMessage.value = "WhatsApp no está instalado"
        }
    }

    fun makeCall(context: Context, phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phoneNumber")
        }
        context.startActivity(intent)
    }
}

