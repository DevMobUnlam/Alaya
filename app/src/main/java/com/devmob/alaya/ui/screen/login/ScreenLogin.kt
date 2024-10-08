package com.devmob.alaya.ui.screen.login

import android.util.Log
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.devmob.alaya.R
import com.devmob.alaya.utils.NavUtils


@Composable
fun SreenLogin(navController: NavController,
               viewModel: LoginViewModel){

    val  showLoginForm = rememberSaveable {
        mutableStateOf(true)
    }

    Surface (modifier = Modifier
        .fillMaxSize()
    ){
        Column (horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center)
        {
            Image(painterResource(id = R.drawable.logounologin), contentDescription = "Logo",
                modifier = Modifier.size(230.dp) )

            Spacer(modifier = Modifier.height(32.dp))
            if (showLoginForm.value) {
                Text(text = "Iniciar sesion")
                UserForm (isCreateAccount = false){
                        email, password ->
                    Log.d("Logeado", "Logeado con $email y $password")
                    viewModel.singInWithEmailAndPassword(email, password,{
                        //Ruta para ir a la Home cuando el login es paciente
                        navController.navigate(NavUtils.PatientRoutes.Home.route) {
                            popUpTo(NavUtils.PatientRoutes.Home.route) {
                                inclusive = true
                            }
                        }
                    }, {//Ruta para ir a la Home cuando el login es profesional
                        //TODO cambiar ruta a professional
                        navController.navigate(NavUtils.ProfessionalRoutes.Home.route) {
                            popUpTo(NavUtils.ProfessionalRoutes.Home.route) {
                                inclusive = true
                            }
                        }
                    })
                }
            }
            else{
                Text(text = "Crear una cuenta")
                UserForm(
                    isCreateAccount = true
                ){
                        email, password ->
                    Log.d("Logeado", "Creando cuenta con $email y $password")
                    viewModel.createUserWithEmailAndPassword(email,password){
                        //Va directo al Home cuando creás una cuenta válida
                        navController.navigate(NavUtils.PatientRoutes.Home.route) {
                            popUpTo(NavUtils.PatientRoutes.Home.route) {
                                inclusive = true
                            }
                        }
                    }
                }

            }
            Spacer(modifier = Modifier.height(15.dp))
            Row (horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically){
                val  text1 =
                    if (showLoginForm.value) "¿No tenes cuenta?"
                    else "¿Ya tienes cuenta?"

                val  text2 =
                    if (showLoginForm.value) "Registrate"
                    else "Iniciar Sesion"
                Text(text = text1)
                Text(text = text2,
                    modifier = Modifier
                        .clickable { showLoginForm.value = !showLoginForm.value }
                        .padding(start = 5.dp))
            }
        }
    }

}

@Composable
fun UserForm(isCreateAccount: Boolean = false,
             onDone: (String, String) -> Unit = {email, pwd ->}
) {
    val  email = rememberSaveable {
        mutableStateOf("")
    }
    val password = rememberSaveable {
        mutableStateOf("")
    }
    val passwordVisible = rememberSaveable {
        mutableStateOf(false)
    }
    val valido = remember(email.value, password.value) {
        email.value.trim().isNotEmpty() &&
                password.value.trim().isNotEmpty()
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        EmailInput (
            emailState = email
        )
        PasswordInput(
            passwordState = password,
            labelId = "Password",
            passwordVisible = passwordVisible
        )
        SubmitButton(
            textId = if (isCreateAccount) "Crear cuenta" else "Login",
            inputValido = valido
        ){
            onDone(email.value.trim(), password.value.trim())
            keyboardController?.hide()
        }
    }
}

@Composable
fun SubmitButton(
    textId: String,
    inputValido: Boolean,
    onClic: ()->Unit
) {
    Button(onClick = onClic,
        modifier = Modifier
            .padding(3.dp)
            .fillMaxWidth(),
        shape =  CircleShape,
        enabled = inputValido) {
        Text(text = textId,
            modifier = Modifier.padding(5.dp))
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
        onValueChange ={passwordState.value = it},
        label = { Text(text = labelId)},
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password
        ),
        modifier = Modifier
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth(),
        visualTransformation = visualTransformation,
        trailingIcon = {
            if (passwordState.value.isNotBlank()){
                PasswordVisibleIcon(passwordVisible)
            }
            else null
        }
    )
}

@Composable
fun PasswordVisibleIcon(
    passwordVisible: MutableState<Boolean>) {
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
fun EmailInput(emailState: MutableState<String>,
               labelId: String = "Email") {
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
    keyboardType: KeyboardType) {
    OutlinedTextField(
        value =valueState.value ,
        onValueChange = {valueState.value = it},
        label = { Text(text = labelId)},
        singleLine = isSingLine,
        modifier = Modifier
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth(),
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType)
    )
}