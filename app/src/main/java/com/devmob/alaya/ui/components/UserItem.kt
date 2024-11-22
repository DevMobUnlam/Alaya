package com.devmob.alaya.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.devmob.alaya.domain.model.Patient
import com.devmob.alaya.ui.theme.ColorPrimary
import com.devmob.alaya.ui.theme.ColorText
import com.devmob.alaya.utils.toHourString

@Composable
fun UserItem(patient: Patient, withSubtitle: Boolean, onClick: () -> Unit) {
    val imageUrl = patient.profileImage
    val initials = if (imageUrl.isNullOrEmpty()) {
        "${patient.name.firstOrNull()?.toString() ?: ""}${patient.surname.firstOrNull()?.toString() ?: ""}"
    } else {
        ""
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        if (imageUrl == null) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color.Gray),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = initials,
                    color = Color.White
                )
            }
        } else {
            Image(
                painter = rememberAsyncImagePainter(model = imageUrl),
                contentDescription = "Foto de ${patient.name}",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = "${patient.name} ${patient.surname}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = ColorText
            )
            if (withSubtitle) {
                patient.nextSession?.let {
                    Text(
                        text = it.toHourString(),
                        fontSize = 18.sp,
                        color = ColorPrimary
                    )
                }
            }
        }
    }
}