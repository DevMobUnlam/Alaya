package com.devmob.alaya.model

data class ItemMenu(
    val title: String? = null,
    val iconType: IconType,
    val route: String,
    val contentDescription: String
)

enum class IconType {
    HOME, PATIENT, PROFESSIONAL, MENU
}
