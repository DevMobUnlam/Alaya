package com.devmob.alaya.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.devmob.alaya.R
import com.devmob.alaya.model.IconType
import com.devmob.alaya.model.ItemMenu
import com.devmob.alaya.ui.theme.ColorPrimary
import com.devmob.alaya.ui.theme.ColorTertiary
import com.devmob.alaya.ui.theme.ColorText
import com.devmob.alaya.ui.theme.ColorWhite

@Composable
fun BottomBarNavigation(items: List<ItemMenu>, navHostController: NavHostController) {
    val currentRoute = currentRoute(navHostController)
    NavigationBar(containerColor = ColorWhite) {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route && !isMiddleButton(item.iconType),
                onClick = {
                    navHostController.navigate(item.route) {
                        popUpTo(navHostController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                    }
                },
                icon = {
                    IconMenu(item, navHostController)
                },
                label = { item.title?.let { Text(text = it) } }
            )
        }
    }
}

@Composable
private fun currentRoute(navHostController: NavHostController): String? {
    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}

private fun isMiddleButton(type: IconType) =
    type == IconType.PROFESSIONAL || type == IconType.PATIENT

@Composable
fun IconMenu(item: ItemMenu, navHostController: NavHostController) {
    when (item.iconType) {
        IconType.HOME -> Icon(
            Icons.Default.Home,
            contentDescription = "Home",
            tint = ColorText,
            modifier = Modifier.size(48.dp)
        )

        IconType.MENU -> Icon(
            Icons.Default.Menu,
            contentDescription = "Menu",
            tint = ColorText,
            modifier = Modifier.size(48.dp)
        )

        IconType.PROFESSIONAL -> FloatingMiddleButton(item, navHostController)
        IconType.PATIENT -> FloatingMiddleButtonWithAnimation(item, navHostController)
    }
}

@Composable
fun FloatingMiddleButton(item: ItemMenu, navHostController: NavHostController) {
    FloatingActionButton(
        containerColor = ColorTertiary,
        modifier = Modifier
            .size(60.dp),
        onClick = { navHostController.navigate(item.route) },
        shape = CircleShape
    ) {
        when (item.iconType) {
            IconType.PATIENT -> Image(
                painter = painterResource(id = R.mipmap.ic_patient),
                contentDescription = "manejo de crisis",
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(48.dp)
            )

            IconType.PROFESSIONAL -> Icon(
                Icons.Outlined.AccountCircle,
                contentDescription = "pacientes",
                modifier = Modifier.size(48.dp),
                tint = ColorWhite
            )

            else -> {}
        }
    }
}

@Composable
fun FloatingMiddleButtonWithAnimation(item: ItemMenu, navHostController: NavHostController) {
    val scale = remember { Animatable(1f) }

    LaunchedEffect(Unit) {
        scale.animateTo(
            targetValue = 1.1f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 1000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            )
        )
    }

    Box(contentAlignment = Alignment.Center) {
        Box(
            modifier = Modifier
                .size(70.dp)
                .graphicsLayer {
                    scaleX = scale.value
                    scaleY = scale.value
                }
                .background(ColorPrimary, shape = CircleShape)
        )
        FloatingMiddleButton(item, navHostController)
    }
}

@Preview
@Composable
fun NavigationBarPreview() {
    val navHostController = rememberNavController()
    Scaffold(
        bottomBar = {
            BottomBarNavigation(
                items = listOf(
                    ItemMenu(iconType = IconType.HOME, route = "home"),
                    ItemMenu(iconType = IconType.PATIENT, route = "manejo de crisis"),
                    ItemMenu(iconType = IconType.MENU, route = "menu hamburguesa")
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
