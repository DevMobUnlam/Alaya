package com.devmob.alaya.ui.screen.ContainmentNetwork.Contact

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.devmob.alaya.domain.model.Contact
import com.devmob.alaya.ui.components.Button
import java.util.UUID
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import com.devmob.alaya.ui.components.IconButton
import com.devmob.alaya.ui.components.Input
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.width
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberAsyncImagePainter
import com.devmob.alaya.ui.screen.ContainmentNetwork.ContainmentNetworkViewModel

@Composable
fun AddContactScreen(
    viewModel: ContainmentNetworkViewModel,
    navController: NavController,
){
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var photoUri by remember { mutableStateOf<Uri?>(null) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        photoUri = uri
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (photoUri != null) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = photoUri,
                    contentScale = ContentScale.Crop,
                ),
                contentScale = ContentScale.Crop,
                contentDescription = "Imagen de tarjeta",
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(16.dp))
        } else {
            //IconButton(symbol = Icons.Outlined.CameraAlt, onClick = { galleryLauncher.launch("image/*") })

        }
        Input(
            value = name,
            onValueChange = { name = it },
            label = "Nombre",
            placeholder = "Ingresa el nombre",
        )

        Spacer(modifier = Modifier.height(16.dp))

        Input(
            value = phone,
            onValueChange = { phone = it },
            label = "Teléfono",
            placeholder = "Ingresa el número de teléfono",
            keyboardType = KeyboardType.Phone
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (name.isNotBlank() && phone.isNotBlank()) {
                    val newContact = Contact(
                        contactId = UUID.randomUUID().toString(),
                        name = name,
                        numberPhone = phone,
                        photo = photoUri?.toString()
                    )
                    viewModel.addContact(newContact)
                    navController.popBackStack()
                }
            },
            text = "Guardar"
        )
    }
}