package com.devmob.alaya.ui.screen.CustomActivity

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.devmob.alaya.R
import com.devmob.alaya.domain.model.OptionTreatment
import com.devmob.alaya.ui.components.Input
import com.devmob.alaya.ui.screen.professionalCrisisTreatment.ConfigTreatmentViewModel
import com.devmob.alaya.ui.theme.ColorPrimary
import com.devmob.alaya.ui.theme.ColorText
import com.devmob.alaya.ui.theme.ColorWhite
import com.devmob.alaya.ui.theme.LightBlueColor
import com.devmob.alaya.utils.NavUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomActivityScreen(
    patientEmail: String,
    navController: NavController,
    viewModel: ConfigTreatmentViewModel
) {
    var title by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var imageUri by rememberSaveable { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        val (inputTitle, inputDescription, imageBox, spacer, saveButton) = createRefs()

        Image(
            painter = painterResource(id = R.drawable.fondo_home),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Input(
            value = title,
            onValueChange = { title = it },
            label = "Actividad",
            placeholder = "Título de la actividad",
            modifier = Modifier.constrainAs(inputTitle) {
                top.linkTo(parent.top, margin = 16.dp)
                start.linkTo(parent.start, margin = 16.dp)
                end.linkTo(parent.end, margin = 16.dp)
            }
        )

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Descripción", color = Color.Gray) },
            placeholder = { Text("Descripción de la Actividad", color = Color.LightGray) },
            modifier = Modifier
                .constrainAs(inputDescription) {
                    top.linkTo(inputTitle.bottom, margin = 16.dp)
                    start.linkTo(parent.start, margin = 16.dp)
                    end.linkTo(parent.end, margin = 16.dp)
                }
                .fillMaxWidth(0.9f)
                .height(150.dp),
            maxLines = 6,
            singleLine = false,
            textStyle = LocalTextStyle.current.copy(lineHeight = 20.sp),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = ColorPrimary,
                focusedLabelColor = ColorPrimary,
                containerColor = ColorWhite,
                unfocusedIndicatorColor = Color.LightGray,
            ),

            )
        Box(
            modifier = Modifier
                .constrainAs(imageBox) {
                    top.linkTo(inputDescription.bottom, margin = 16.dp)
                    start.linkTo(parent.start, margin = 16.dp)
                    end.linkTo(parent.end, margin = 16.dp)
                    width = Dimension.fillToConstraints
                }
                .height(200.dp)
                .background(LightBlueColor, RoundedCornerShape(8.dp))
                .clickable { launcher.launch("image/*") }
                .border(width = 1.dp, color = Color.LightGray)
        ) {
            if (imageUri == null) {
                Icon(
                    imageVector = Icons.Default.AddPhotoAlternate,
                    contentDescription = "Agregar Imagen",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(48.dp),
                    tint = ColorText
                )
            } else {
                Image(
                    painter = rememberImagePainter(imageUri),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(8.dp))
                )
            }
        }
        Spacer(modifier = Modifier
            .constrainAs(spacer) {
                top.linkTo(imageBox.bottom, margin = 16.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            }
            .height(120.dp)
        )

        Button(
            onClick = {
                viewModel.addCustomActivity(OptionTreatment(title, description, imageUri))
                navController.navigate(
                    NavUtils.ProfessionalRoutes.ConfigTreatment.route.replace(
                        "{patientEmail}",
                        patientEmail
                    )
                )
            },
            modifier = Modifier
                .constrainAs(saveButton) {
                    start.linkTo(parent.start, margin = 16.dp)
                    end.linkTo(parent.end, margin = 16.dp)
                    bottom.linkTo(parent.bottom)
                }
                .padding(16.dp)
                .fillMaxWidth(),
            enabled = title.isNotEmpty() && description.isNotEmpty()
        ) {
            Text("Guardar Actividad")
        }
    }
}