package com.devmob.alaya.ui.components


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import com.devmob.alaya.R
import com.devmob.alaya.ui.theme.ColorText

//Sacar dsps de verificar correcto funcionamiento
data class User (val name: String, val image: Int)


@Composable
fun SearchUser() {
    var searchText by remember { mutableStateOf(TextFieldValue("")) }
    val Users = listOf(
        User("Ana Pérez", R.drawable.ana_perez),
        User("Brenda Rodríguez", R.drawable.brenda_rodriguez),
        User("Claudia García", R.drawable.claudia_garcia),
        User("Ezequiel Torres", R.drawable.ezequiel_torres),
        User("Federico Álvarez", R.drawable.federico_alvarez),
        User("Lucía Sánchez", R.drawable.lucia_sanchez),
        User("Matías Ramírez", R.drawable.matias_ramirez),
        User("Mónica Fernández", R.drawable.monica_fernandez),
        User("Sergio Suárez", R.drawable.sergio_suarez),
        User("Valeria Acosta", R.drawable.valeria_acosta),
    )

    val UsersFilter = Users.filter {
        it.name.contains(searchText.text, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color(0xFFF5F8FB))
    ) {
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF1F1F1), shape = RoundedCornerShape(40.dp))
                        .padding(12.dp)
                ) {
                    BasicTextField(
                        value = searchText,
                        onValueChange = { searchText = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        decorationBox = { innerTextField ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Icono de búsqueda",
                                    tint = Color.Gray,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Box(Modifier.weight(1f)) {
                                    if (searchText.text.isEmpty()) {
                                        Text("Buscar pacientes", color = Color.LightGray)
                                    }
                                    innerTextField()
                                }
                            }
                        }
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(UsersFilter) { user ->
                UserItem(user)
            }
        }
    }
}

@Composable
fun UserItem(user: User) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(Color.White, shape = RoundedCornerShape(20.dp))
            .border(
                border = BorderStroke(0.05.dp, Color.White.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(30.dp)
            )
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
        Text(
            text = user.name,
            fontSize = 18.sp,
            color = ColorText
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSearchUser() {
    SearchUser()
}




