package com.devmob.alaya.model

data class ItemMenu(val title: String, val iconType: IconType, val route: String)

enum class IconType {
    HOME, PATIENT, PROFESSIONAL, MENU
}
