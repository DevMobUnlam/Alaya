package com.devmob.alaya.ui.screen.CustomActivity

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.devmob.alaya.domain.model.OptionTreatment
import com.devmob.alaya.ui.screen.ProfessionalTreatment.ConfigTreatmentViewModel
import com.devmob.alaya.utils.NavUtils

@Composable
fun CustomActivityScreen(
    navController: NavController,
    viewModel: ConfigTreatmentViewModel,
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Título de la Actividad") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                TextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descripción de la Actividad") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 5
                )
            }
            item {
                Button(
                    onClick = {
                         launcher.launch("image/*")
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = if (imageUri != null) "Cambiar Imagen" else "Seleccionar Imagen")
                }
            }
            item {
                imageUri?.let {
                    Image(
                        painter = rememberImagePainter(it),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                }
            }
        }

        Button(
            onClick = {
                viewModel.addCustomActivity(OptionTreatment(title, description))
                navController.navigate(NavUtils.ProfessionalRoutes.ConfigTreatment.route)
            },
            modifier = Modifier
                .align(Alignment.Center)
                .padding(16.dp)
                .fillMaxWidth(),
            enabled = title.isNotEmpty() && description.isNotEmpty()
        ) {
            Text("Guardar Actividad")
        }
    }
}