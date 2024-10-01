package com.devmob.alaya.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Mood
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.devmob.alaya.ui.theme.ColorQuaternary
import com.devmob.alaya.ui.theme.ColorTertiary
import com.devmob.alaya.ui.theme.ColorText
import com.devmob.alaya.ui.theme.ColorWhite

@Composable
fun Card(
    title: String,
    subtitle: String? = null,
    imageUrl: String? = null,
    progress: Float? = null,
    onClick: () -> Unit,
    leftIcon: ImageVector? = null,
    rightIcon: ImageVector? = null,
) {
    ElevatedCard(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp).background(color = ColorWhite),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        elevation = CardDefaults.cardElevation(8.dp),

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween

        ) {

            LeftIconOrProgress(progress,leftIcon);

            if (imageUrl != null) {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = imageUrl,
                        contentScale = ContentScale.Crop,
                    ),
                    contentScale = ContentScale.Crop,
                    contentDescription = "Imagen de tarjeta",
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(16.dp))
            }


            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 18.sp, color= ColorText)
                if (subtitle != null) {
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(text = subtitle, fontSize = 14.sp, color = ColorText)
                }
            }
            if (rightIcon != null) {
                Spacer(modifier = Modifier.width(16.dp))
                Icon(
                    imageVector = rightIcon,
                    contentDescription = "Right Icon",
                    tint = ColorText,
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.CenterVertically)
                )


            }
    }}
}

@Composable
fun CardsExample() {
    Column {
        Card(
            title = "¿Cómo me siento hoy?",
            subtitle = "Evalúa tu estado emocional",
            onClick = {  },
            leftIcon = Icons.Outlined.Mood,
            rightIcon = Icons.Filled.ArrowForwardIos
        )

        Card(
            title = "Patricia Psicóloga",
            imageUrl = "https://img.freepik.com/foto-gratis/vista-frontal-mujer-probando-colores_23-2150538715.jpg?ga=GA1.2.509011693.1726456853&semt=ais_hybrid",
            onClick = { }
        )

        Card(
            title = "Salir a caminar",
            subtitle = "3 veces por semana",
            progress = 0.75f,
            onClick = {  }
        )
    }
}

@Composable
fun LeftIconOrProgress(progress: Float?, leftIcon: ImageVector?) {
    when {
        // Si hay progreso, mostrar CircularProgressIndicator o Check
        progress != null -> {
            Box(
                modifier = Modifier.size(40.dp),
                contentAlignment = Alignment.Center
            ) {
                if (progress < 1f) {
                    CircularProgressIndicator(
                        progress = progress,
                        modifier = Modifier.size(36.dp),
                        strokeWidth = 4.dp,
                        color = ColorTertiary,
                        trackColor = ColorQuaternary
                    )
                } else {
                    Icon(
                        imageVector = Icons.Outlined.Check,
                        contentDescription = "Completed",
                        tint = ColorTertiary,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
        }

        leftIcon != null -> {
            Icon(
                imageVector = leftIcon,
                contentDescription = "Left Icon",
                tint = ColorText,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCards() {
    CardsExample()
}