package com.devmob.alaya.ui.components

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.devmob.alaya.ui.screen.ContainmentNetwork.Contact.ContactViewModel
import com.devmob.alaya.ui.theme.ColorPrimary
import com.devmob.alaya.ui.theme.ColorQuaternary
import com.devmob.alaya.ui.theme.ColorText
import com.devmob.alaya.ui.theme.ColorWhite
import java.io.Serializable
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun NextAppointmentHeader(
    name: String,
    lastName: String,
    date: Serializable?,
    modifier: Modifier,
    contactViewModel: ContactViewModel,
    phoneNumber: String?,
    context: Context,
    image: String
) {

    val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val timeFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())

    val formattedDate = if (date != null) dateFormatter.format(date) else null
    val formattedTime = if (date != null) timeFormatter.format(date) else null

    Card(
        colors = CardDefaults.cardColors(containerColor = ColorWhite),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 9.dp
        ),
        modifier = modifier
            .height(IntrinsicSize.Min)
            .padding(16.dp),
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SubcomposeAsyncImage(
                model = image,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape),
                loading = {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = ColorPrimary
                        )
                    }
                },
                error = {
                    Icon(
                        imageVector = Icons.Rounded.AccountCircle,
                        tint = Color.Gray,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize()
                    )
                })

            Spacer(modifier = Modifier.width(8.dp))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "$name $lastName",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = ColorText,
                    textAlign = TextAlign.Center,
                )
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    color = ColorQuaternary,
                    thickness = 3.dp
                )
                Spacer(modifier = Modifier.height(8.dp))
                if (formattedDate != null && formattedTime != null) {
                    Text(
                        text = "Próxima Sesión:",
                        fontSize = 21.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        color = ColorText,
                    )
                    Text(
                        text = "$formattedDate - \n $formattedTime hs",
                        fontSize = 21.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        color = ColorText,
                    )
                } else {

                    Text(
                        text = "No tienes una próxima sesión programada",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        color = ColorText,
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                WhatsAppButton(contactViewModel, phoneNumber ?: "", context)
            }

        }
    }
}

fun formatDate(date: LocalDateTime, pattern: String = "dd/MM/yyyy"): String {
    val formatter = DateTimeFormatter.ofPattern(pattern)
    return date.format(formatter)
}

fun formatTime(date: LocalDateTime, pattern: String = "HH:mm"): String {
    val formatter = DateTimeFormatter.ofPattern(pattern)
    return date.format(formatter)
}

