package com.devmob.alaya.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
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
import com.devmob.alaya.ui.components.CardContainer
import com.devmob.alaya.ui.theme.ColorText
import com.devmob.alaya.utils.NavUtils

@Composable
fun MenuScreen(navController: NavController){
    Column(
        modifier = Modifier
            .fillMaxSize()
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
                    modifier = Modifier.padding(18.dp).fillMaxWidth().clickable {
                        navController.navigate(
                            NavUtils.Routes.Login.route
                        )
                    }
                )
            }
        )
    }
}