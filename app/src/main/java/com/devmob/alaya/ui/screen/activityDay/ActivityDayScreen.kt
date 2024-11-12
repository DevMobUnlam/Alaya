package com.devmob.alaya.ui.screen.activityDay

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout

@Composable
fun ActivityDayScreen(){
    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ){
        Text("Hola como estas")

    }

}

@Preview(showBackground = true)
@Composable
fun PreviewActivityDay(){
    ActivityDayScreen()
}