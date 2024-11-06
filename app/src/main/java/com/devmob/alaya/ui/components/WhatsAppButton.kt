package com.devmob.alaya.ui.components

import android.content.Context
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.devmob.alaya.R
import com.devmob.alaya.ui.screen.ContainmentNetwork.Contact.ContactViewModel
import androidx.compose.material3.Button

@Composable
fun WhatsAppButton(
    contactViewModel: ContactViewModel,
    phoneNumber: String,
    context: Context
) {
    Button(
        onClick = {
            val formattedNumber = contactViewModel.formatPhoneNumberForWhatsApp(phoneNumber)
            contactViewModel.sendWhatsAppMessage(context, formattedNumber, "Hola, ¿cómo estás?")
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF25D366)
        )
    ) {
        Icon(
            painter = painterResource(id = R.drawable.whatsapp),
            contentDescription = "WhatsApp",
            modifier = Modifier.size(24.dp),
            tint = Color.White,
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "Enviar mensaje", color = Color.White)
    }
}