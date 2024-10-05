package com.devmob.alaya.screen

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.outlined.Checklist
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Mood
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import com.devmob.alaya.R
import com.devmob.alaya.components.Card
import com.devmob.alaya.ui.theme.ColorText

@Composable
fun HomeScreen(navController: NavController) {
    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {
        val (backgroundImage, greetingText, cardColumn) = createRefs()

        Image(
            painter = painterResource(id = R.drawable.fondo_home),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .constrainAs(backgroundImage) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            contentScale = ContentScale.Crop
        )

        Text(
            text = "Hola Flor, ¡Buen día!",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = ColorText,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .constrainAs(greetingText) {
                    top.linkTo(parent.top, margin = 60.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )

        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .constrainAs(cardColumn) {
                    top.linkTo(greetingText.bottom, margin = 24.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom, margin = 70.dp)
                    width = Dimension.fillToConstraints

                }
        ) {
            Card(
                title = "¿Cómo me siento hoy?",
                subtitle = "Evalúa tu estado emocional",
                onClick = { },
                leftIcon = Icons.Outlined.Mood,
                rightIcon = Icons.Filled.ArrowForwardIos
            )
            Card(
                title = "Mi red de contención",
                subtitle = "Accede a tus contactos de confianza",
                onClick = { },
                leftIcon = Icons.Outlined.Groups,
                rightIcon = Icons.Filled.ArrowForwardIos
            )
            Card(
                title = "Actividades diarias",
                subtitle = "Prácticas diarias para mejorar el control emocional",
                onClick = { },
                leftIcon = Icons.Outlined.Checklist,
                rightIcon = Icons.Filled.ArrowForwardIos
            )
            Card(
                title = "Herramientas de bienestar",
                subtitle = "Encuentra recursos para cuidar tu mente y mejorar tu bienestar diario",
                onClick = { },
                rightIcon = Icons.Filled.ArrowForwardIos,
                leftIconBitmap = BitmapFactory.decodeResource(
                        LocalContext.current.resources,
                R.drawable.hand_heart
            )
            )
            Card(
                title = "Registro de crisis",
                subtitle = "Registra detalles del episodio para entender y mejorar tu manejo en estos momentos",
                onClick = { },
                leftIcon = Icons.Outlined.Mood,
                rightIcon = Icons.Filled.ArrowForwardIos,
                leftIconBitmap = BitmapFactory.decodeResource(
                    LocalContext.current.resources,
                    R.drawable.head_side_heart
                )
            )
        }
    }
}




