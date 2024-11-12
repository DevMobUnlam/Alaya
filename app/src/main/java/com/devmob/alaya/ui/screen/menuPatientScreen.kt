package com.devmob.alaya.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.devmob.alaya.data.FirebaseClient
import com.devmob.alaya.data.preferences.SharedPreferences
import com.devmob.alaya.ui.components.CardContainer
import com.devmob.alaya.ui.theme.ColorText
import com.devmob.alaya.ui.theme.LightBlueColor
import com.devmob.alaya.utils.NavUtils

@Composable
fun MenuPatientScreen(navController: NavController, prefs: SharedPreferences){
    val auth = FirebaseClient().auth
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LightBlueColor)
            .padding(16.dp)
    ) {
        CardContainer(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                ,
            enabled = true,
            content = {
                Text(
                    text = "Cerrar sesión",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = ColorText,
                    modifier = Modifier
                        .padding(18.dp)
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate(
                                NavUtils.LoginRoutes.Login.route

                            ){
                                popUpTo(NavUtils.LoginRoutes.Login.route) {
                                    inclusive = true
                                    saveState = false
                                }
                            }
                            auth.signOut()
                            prefs.signOut()
                        }
                )
            }
        )
    }
}