package com.devmob.alaya.components


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
import androidx.compose.material3.Icon
import androidx.compose.ui.draw.clip
import com.devmob.alaya.R


// Datos de usuarios para mostrar en la lista
data class Usuario(val nombre: String, val imagen: Int)

@Composable
fun BuscadorUsuarios() {
    var searchText by remember { mutableStateOf(TextFieldValue("")) }
    val usuarios = listOf(
        Usuario("Ana Pérez", R.drawable.ic_launcher_foreground),  // Reemplaza las imágenes con tus recursos
        Usuario("Brenda Rodríguez",R.drawable.ic_launcher_foreground),
        Usuario("Claudia García",R.drawable.ic_launcher_foreground),
        Usuario("Ezequiel Torres", R.drawable.ic_launcher_foreground),
        Usuario("Federico Álvarez", R.drawable.ic_launcher_foreground),
        Usuario("Lucía Sánchez", R.drawable.ic_launcher_foreground),
        Usuario("Mateos Ramírez", R.drawable.ic_launcher_foreground),
        Usuario("Mónica Fernández", R.drawable.ic_launcher_foreground),
        Usuario("Sergio Suárez", R.drawable.ic_launcher_foreground),
        Usuario("Valeria Aceves", R.drawable.ic_launcher_foreground)
    )

    // Filtrar la lista según el texto de búsqueda
    val usuariosFiltrados = usuarios.filter {
        it.nombre.contains(searchText.text, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEAF2FD))  // Fondo azul claro como en la imagen
            .padding(16.dp)
    ) {
        // Campo de texto para buscar
        BasicTextField(
            value = searchText,
            onValueChange = { searchText = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(Color.White, shape = RoundedCornerShape(16.dp))  // Borde redondeado
                .padding(horizontal = 16.dp, vertical = 12.dp),  // Margen interno
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
                            Text("Buscar pacientes", color = Color.Gray)  // Texto de ayuda
                        }
                        innerTextField()
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Lista de usuarios
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(usuariosFiltrados) { usuario ->
                UsuarioItem(usuario)
            }
        }
    }
}

@Composable
fun UsuarioItem(usuario: Usuario) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(Color.White, shape = RoundedCornerShape(8.dp))  // Fondo blanco con bordes redondeados
            .border(
                border = BorderStroke(1.dp, Color.Gray.copy(alpha = 0.5f)), // Borde con color gris claro
                shape = RoundedCornerShape(8.dp)
            )
            .padding(8.dp)
    ) {
        Image(
            painter = painterResource(usuario.imagen),
            contentDescription = "Foto de ${usuario.nombre}",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)  // Imagen circular
                .border(1.dp, Color.Gray, CircleShape)  // Borde alrededor de la imagen
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = usuario.nombre,
            fontSize = 18.sp,
            color = Color.Black
        )
    }
}