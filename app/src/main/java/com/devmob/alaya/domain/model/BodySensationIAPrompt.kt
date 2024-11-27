package com.devmob.alaya.domain.model

import com.google.gson.annotations.SerializedName

data class BodySensationIAPrompt(
    @SerializedName("name")
    val name: String = "",
    @SerializedName("intensity")
    val intensity: String = ""
)
