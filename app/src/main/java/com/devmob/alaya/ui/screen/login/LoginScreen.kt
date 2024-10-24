package com.devmob.alaya.ui.screen.login

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.devmob.alaya.R
import com.devmob.alaya.ui.theme.ColorPrimary
import com.devmob.alaya.utils.NavUtils


@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel
) {

    val context = LocalContext.current
    Log.d("leandro", "Log del loginScreen")
    var checkLogin by rememberSaveable { mutableStateOf(false)}


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

    LaunchedEffect(!checkLogin) {
        viewModel.checkIfUserWasLoggedIn()
        checkLogin = true
    }

    if (viewModel.loading.value) {
        Surface(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = ColorWhite
                    ),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = ColorPrimary
                )
            }
        }
    } else {
        Surface(
            modifier = Modifier
                .fillMaxSize()
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(30.dp)
            )
            {
                Image(
                    painterResource(id = R.drawable.logounologin),
                    contentDescription = stringResource(R.string.logo),
                    modifier = Modifier.size(230.dp)
                )

            Spacer(modifier = Modifier.height(32.dp))
            UserForm { email, password ->
                Log.d(javaClass.name, context.getString(R.string.logeado_con_correctamente))
                viewModel.singInWithEmailAndPassword(email, password)
            }

            Spacer(modifier = Modifier.height(15.dp))
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = stringResource(R.string.no_tenes_cuenta))
                Text(text = stringResource(R.string.registrate),
                    color = ColorPrimary,
                    modifier = Modifier
                        .clickable {
                            navController.navigate(NavUtils.LoginRoutes.Register.route) {
                                popUpTo(NavUtils.LoginRoutes.Register.route) {
                                    inclusive = true
                                }
                            }
                        }
                        .padding(start = 5.dp))
            }
        }
    }
}


@Composable
fun UserForm(
    onDone: (String, String) -> Unit = { email, pwd -> }
) {
    val email = rememberSaveable {
        mutableStateOf("")
    }
    val password = rememberSaveable {
        mutableStateOf("")
    }
    val passwordVisible = rememberSaveable {
        mutableStateOf(false)
    }
    val valid = remember(email.value, password.value) {
        email.value.trim().isNotEmpty() &&
                password.value.trim().isNotEmpty()
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        EmailInput(
            emailState = email
        )
        PasswordInput(
            passwordState = password,
            labelId = stringResource(R.string.password),
            passwordVisible = passwordVisible
        )
        SubmitButton(
            textId = stringResource(R.string.iniciar_sesi_n),
            inputValid = valid
        ) {
            onDone(email.value.trim(), password.value.trim())
            keyboardController?.hide()
        }
    }
}

@Composable
fun SubmitButton(
    textId: String,
    inputValid: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(3.dp)
            .fillMaxWidth(),
        shape = CircleShape,
        enabled = inputValid
    ) {
        Text(
            text = textId,
            modifier = Modifier.padding(5.dp)
        )
    }
}

@Composable
fun PasswordInput(
    passwordState: MutableState<String>,
    labelId: String,
    passwordVisible: MutableState<Boolean>
) {
    val visualTransformation = if (passwordVisible.value)
        VisualTransformation.None
    else PasswordVisualTransformation()

    OutlinedTextField(
        value = passwordState.value,
        onValueChange = { passwordState.value = it },
        label = { Text(text = labelId) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password
        ),
        modifier = Modifier
            .padding(bottom = 10.dp)
            .fillMaxWidth(),
        visualTransformation = visualTransformation,
        trailingIcon = {
            if (passwordState.value.isNotBlank()) {
                PasswordVisibleIcon(passwordVisible)
            }
        }
    )
}

@Composable
fun PasswordVisibleIcon(
    passwordVisible: MutableState<Boolean>
) {
    val image =
        if (passwordVisible.value)
            Icons.Default.VisibilityOff
        else
            Icons.Default.Visibility
    IconButton(onClick = {
        passwordVisible.value = !passwordVisible.value
    }) {
        Icon(
            imageVector = image,
            contentDescription = ""
        )
    }
}


@Composable
fun EmailInput(
    emailState: MutableState<String>,
    labelId: String = "Email"
) {
    InputField(
        valueState = emailState,
        labelId = labelId,
        keyboardType = KeyboardType.Email
    )
}

@Composable
fun InputField(
    valueState: MutableState<String>,
    labelId: String,
    isSingLine: Boolean = true,
    keyboardType: KeyboardType
) {
    OutlinedTextField(
        value = valueState.value,
        onValueChange = { valueState.value = it },
        label = { Text(text = labelId) },
        singleLine = isSingLine,
        modifier = Modifier
            .padding(bottom = 10.dp)
            .fillMaxWidth(),
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        )
    )
}