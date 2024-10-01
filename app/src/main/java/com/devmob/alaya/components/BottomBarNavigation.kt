package com.devmob.alaya.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.devmob.alaya.model.IconType
import com.devmob.alaya.model.ItemMenu
import com.devmob.alaya.ui.theme.ColorWhite

@Composable
fun BottomBarNavigation(items: List<ItemMenu>, navHostController: NavHostController) {
    val currentRoute = currentRoute(navHostController)
    NavigationBar(containerColor = ColorWhite) {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    navHostController.navigate(item.route) {
                        popUpTo(navHostController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = when (item.iconType) {
                            IconType.HOME -> Icons.Default.Home
                            IconType.PATIENT -> Icons.Default.AccountCircle
                            IconType.PROFESSIONAL -> Icons.Default.Person
                            IconType.MENU -> Icons.Default.Menu
                        },
                        contentDescription = item.title
                    )
                },
                label = { Text(text = item.title) }
            )
        }
    }
}

@Composable
private fun currentRoute(navHostController: NavHostController): String? {
    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}

@Preview
@Composable
fun NavigationBarPreview() {
    val navHostController = rememberNavController()
    Scaffold(
        bottomBar = {
            BottomBarNavigation(
                items = listOf(
                    ItemMenu("Home", IconType.HOME, "home"),
                    ItemMenu("Manejo de criss", IconType.PROFESSIONAL, "manejo de crisis"),
                    ItemMenu("Más", IconType.MENU, "menu hamburguesa")
                ), navHostController = navHostController
            )
        }
    ) { paddingValues ->
        Text(
            text = "Pantalla Principal",
            modifier = Modifier.padding(paddingValues)
        )
    }
}
