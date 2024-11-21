package com.devmob.alaya.ui.screen.profile_user

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.devmob.alaya.R
import com.devmob.alaya.domain.model.UserRole
import com.devmob.alaya.ui.components.CardContainer
import com.devmob.alaya.ui.components.Input
import com.devmob.alaya.ui.theme.ColorText
import com.devmob.alaya.utils.NavUtils

@Composable
fun ProfileUserScreen(
    viewModel: ProfileUserViewModel,
    navController: NavController
) {
    val userState by viewModel.userState.collectAsState()
    val profileImage by viewModel.profileImage.collectAsState()
    val phoneNumber = remember { mutableStateOf("") }
    val isEditing = remember { mutableStateOf(false) }
    val professional by viewModel.professional.collectAsState()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let {
                viewModel.uploadImageToFirebase(it)
            }
        }
    )

    val context = LocalContext.current
    val toastMessage = remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.fondo_home),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        when (userState) {
            is UserState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is UserState.Error -> {
                Text(
                    text = (userState as UserState.Error).message,
                    color = Color.Red,
                    modifier = Modifier.fillMaxSize().padding(16.dp)
                )
            }
            is UserState.Success -> {
                val user = (userState as UserState.Success).user
                val email = (userState as UserState.Success).user.email

                phoneNumber.value = user.phone

                CardContainer(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                   content = {
                       Column(
                           modifier = Modifier.padding(16.dp),
                           horizontalAlignment = Alignment.CenterHorizontally
                       ) {
                           Box(
                               modifier = Modifier
                                   .size(120.dp)
                                   .clip(CircleShape)
                                   .background(Color(0xFFF5A5DE))
                                   .clickable {
                                       launcher.launch("image/*")
                                   },
                               contentAlignment = Alignment.Center
                           ) {
                               if (profileImage != "") {
                                   Image(
                                       painter = rememberAsyncImagePainter(profileImage),
                                       contentDescription = "Foto de perfil",
                                       modifier = Modifier.fillMaxSize(),
                                       contentScale = ContentScale.Crop
                                   )
                               } else {
                                   Text(
                                       text = "${user.name.firstOrNull() ?: ""}${user.surname.firstOrNull() ?: ""}",
                                       color = Color.White,
                                   )
                               }
                           }

                           Spacer(modifier = Modifier.height(16.dp))

                           Text(
                               text = "${user.name} ${user.surname}",
                               fontWeight = FontWeight.Bold,
                               color = ColorText
                           )
                           Spacer(modifier = Modifier.height(8.dp))

                               Input(
                                   value = phoneNumber.value,
                                   label = "Teléfono",
                                   placeholder = "",
                                   onValueChange = { phoneNumber.value = it },
                                   keyboardType = KeyboardType.Number
                               )
                               Spacer(modifier = Modifier.height(16.dp))

                           if (user.role == UserRole.PATIENT && professional != null) {
                               Spacer(modifier = Modifier.height(16.dp))
                               Text(
                                   text = "Profesional: ${professional?.name} ${professional?.surname}",
                                   fontWeight = FontWeight.Bold,
                                   color = ColorText
                               )
                               Text(
                                   text = "Teléfono: ${professional?.phone}",
                                   color = ColorText
                               )
                           }

                               Button(
                                   onClick = {
                                       isEditing.value = false
                                       viewModel.updatePhoneNumber(phoneNumber.value)
                                       profileImage?.let { viewModel.updateProfileImage(it) }
                                       Toast.makeText(context, "Cambios guardados", Toast.LENGTH_SHORT).show()
                                       if(user.role ==UserRole.PATIENT){
                                           navController.navigate(NavUtils.PatientRoutes.Home.route)
                                       } else if(user.role == UserRole.PROFESSIONAL) {
                                           navController.navigate(NavUtils.ProfessionalRoutes.Home.route)
                                       }
                                   },
                                   modifier = Modifier.align(Alignment.CenterHorizontally)
                               ) {
                                   Text("Guardar")
                               }
                       }
                   }
                )
            }
        }
    }
}