package com.devmob.alaya.domain.model

import com.devmob.alaya.R

data class OptionTreatment(
    val title: String,
    val description: String,
    val imageUri: String,
    val imageResId: Int = R.drawable.logounologin
) {
    constructor() : this("","", "")
}
