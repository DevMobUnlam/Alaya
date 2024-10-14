package com.devmob.alaya.ui.screen.register

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.devmob.alaya.R
import com.devmob.alaya.domain.model.User
import com.devmob.alaya.domain.model.UserRole
import com.devmob.alaya.ui.components.Switch
import com.devmob.alaya.ui.theme.ColorPrimary
import com.devmob.alaya.utils.NavUtils


@Composable
fun RegisterScreen(navController: NavController, viewModel: RegisterViewmodel) {

    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var isProfessional by remember { mutableStateOf(false) }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var showAlert by remember { mutableStateOf(false) }

    if (viewModel.navigateToPatientHome.value) {
        navController.navigate(NavUtils.PatientRoutes.Home.route) {
            popUpTo(NavUtils.PatientRoutes.Home.route) {
                inclusive = true
            }
        }
    }
    if (viewModel.navigateToProfessionalHome.value) {
        navController.navigate(NavUtils.ProfessionalRoutes.Home.route) {
            popUpTo(NavUtils.ProfessionalRoutes.Home.route) {
                inclusive = true
            }
        }
    }

    if (viewModel.showError.value) {
        Toast.makeText(
            context,
            stringResource(R.string.usuario_o_contrasena_invalidos),
            Toast.LENGTH_SHORT
        )
            .show()
        viewModel.resetError()
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(id = R.drawable.pasotresrelajacionnuevo),
            contentDescription = "Imagen de Registro",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Registrar usuario",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            color = ColorPrimary,
            fontWeight = FontWeight.Black

        )
        Spacer(modifier = Modifier.height(16.dp))

        Switch(
            modifier = Modifier.fillMaxWidth(),
            onChange = { isProfessional = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = {
                Text(
                    "Nombre",
                    fontSize = 20.sp
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null
                )
            },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = surname,
            onValueChange = { surname = it },
            label = {
                Text(
                    "Apellido",
                    fontSize = 20.sp
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null
                )
            },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = {
                Text(
                    "Email",
                    fontSize = 20.sp
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = null
                )
            },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = {
                Text(
                    "Contraseña",
                    fontSize = 20.sp
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null
                )
            },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = {
                Text(
                    "Confirmar Contraseña",
                    fontSize = 20.sp
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null
                )
            },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(30.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center // Alineación central horizontal
        ) {
            Button(
                onClick = {
                    // Acción para el botón Cancelar
                },
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ColorPrimary)
            ) {
                Text(
                    "Cancelar",
                    fontSize = 20.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Black
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                onClick = {
                    val newUser = User(
                        name = name,
                        surname = surname,
                        email = email,
                        role = resolveRole(isProfessional)
                    )
                    if (validateRegisterFields(
                            newUser,
                            password,
                            confirmPassword,
                            showAlert,
                            errorMessage
                        )
                    ) {
                        viewModel.createUser(newUser, password)
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ColorPrimary)
            ) {
                Text(
                    "Registrar",
                    fontSize = 20.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Black
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

fun resolveRole(professional: Boolean): UserRole {
    return when (professional) {
        true -> UserRole.PROFESSIONAL
        false -> UserRole.PATIENT
    }

}


private fun validateRegisterFields(
    user: User,
    password: String,
    confirmPassword: String,
    showAlert: Boolean,
    errorMessage: String
): Boolean {
    var showAlert1 = showAlert
    var errorMessage1 = errorMessage
    if (user.name.isEmpty() || user.surname.isEmpty() || user.email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
        showAlert1 = true
        return false
    } else if (password != confirmPassword) {
        errorMessage1 = "Las contraseñas no coinciden"
        return false
    } else {
        errorMessage1 = "Registrado con éxito"
        return true
    }
}
