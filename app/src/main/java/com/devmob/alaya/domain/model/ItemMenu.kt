package com.devmob.alaya.domain.model

data class ItemMenu(
    val title: String? = null,
    val iconType: IconType,
    val route: String,
    val contentDescription: String,
    val order: Int
)

enum class IconType {
    HOME, PATIENT, PROFESSIONAL, MENU
}
