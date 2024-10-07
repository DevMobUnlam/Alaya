package com.devmob.alaya.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.devmob.alaya.R
import com.devmob.alaya.ui.components.CardContainer
import com.devmob.alaya.ui.components.Header
import com.devmob.alaya.ui.theme.ColorQuaternary
import com.devmob.alaya.ui.theme.ColorText

@Composable
fun ProfessionalHomeScreen(navController: NavController) {
    val users = listOf(
        User("Ana Pérez", R.drawable.ic_launcher_foreground,"08:30"),
        User("Brenda Rodríguez", R.drawable.ic_launcher_foreground,"09:30"),
        User("Claudia García", R.drawable.ic_launcher_foreground,"10:30"),
        User("Ezequiel Torres", R.drawable.ic_launcher_foreground,"11:30"),
        User("Federico Álvarez", R.drawable.ic_launcher_foreground,"12:30"),
        User("Lucía Sánchez", R.drawable.ic_launcher_foreground,"13:30"),
        User("Matías Ramírez", R.drawable.ic_launcher_foreground,"15:00"),
        User("Mónica Fernández", R.drawable.ic_launcher_foreground,"16:00"),
        User("Sergio Suárez", R.drawable.ic_launcher_foreground,"17:00"),
        User("Valeria Acosta", R.drawable.ic_launcher_foreground,"18:30"),
        User("Walter Quiroz", R.drawable.ic_launcher_foreground,"19:30")
    )

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Header("Bren")  //Obtener nombre del viewmodel
        Spacer(modifier = Modifier.width(16.dp))
        CardContainer(
            modifier = Modifier.fillMaxHeight(),
            enabled = true,
            content = {
                Column(Modifier.padding(13.dp,0.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    )
                    {
                        Text(
                            text = "Pacientes",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = ColorText
                        )

                        Icon(
                            imageVector = Icons.Filled.ArrowForwardIos,
                            contentDescription = "Right Icon",
                            tint = ColorText,
                            modifier = Modifier
                                .size(24.dp)
                                .align(Alignment.CenterVertically)
                        )
                    }
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        color = ColorQuaternary,
                        thickness = 1.dp)
                    LazyColumn(
                        modifier = Modifier.wrapContentHeight()
                    ) {
                        items(users) { user ->
                            UserItem(user)
                        }
                    }
                }
            }
        )
    }
}

@Composable
fun UserItem(user: User) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Image(
            painter = painterResource(user.image),
            contentDescription = "Foto de ${user.name}",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = user.name,
                fontSize = 18.sp,
                color = ColorText
            )
            Text(
                text = user.hour,
                fontSize = 18.sp,
                color = ColorText
            )
        }

    }
}

data class User(val name: String, val image: Int, val hour: String)

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewProfessionalHomeScreen() {
    val navController = rememberNavController()
    ProfessionalHomeScreen(navController)
}