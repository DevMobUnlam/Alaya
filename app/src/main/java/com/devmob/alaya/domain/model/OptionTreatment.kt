package com.devmob.alaya.domain.model

import android.net.Uri

data class OptionTreatment(
    val title: String,
    val description: String,
    val imageUri: Uri? = null,
    val imageResId: Int? = null
)
