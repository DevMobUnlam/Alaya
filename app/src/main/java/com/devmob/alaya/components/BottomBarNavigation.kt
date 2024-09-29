package com.devmob.alaya.components

import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.devmob.alaya.ui.theme.ColorWhite

@Composable
fun BottomBarNavigation(userType: UserType) {
    NavigationBar(Modifier.background(ColorWhite)) {
        NavigationBarItem(selected = false, onClick = { /*TODO*/ }, icon = {
            Icon(
                imageVector = Icons.Default.Home,
                contentDescription = "home"
            )
        }, label = { Text(text = "Home") })
        NavigationBarItem(selected = false, onClick = { /*TODO*/ }, icon = {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = "manejo de crisis"
            )
        }, label = { Text(text = "Manejo de crisis") })
        NavigationBarItem(selected = false, onClick = { /*TODO*/ }, icon = {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "más"
            )
        }, label = { Text(text = "Más") })
    }
}

enum class UserType {
    PATIENT, PROFESSIONAL
}

@Preview
@Composable
fun NavigationBarPreview() {
    BottomBarNavigation(UserType.PATIENT)
}
