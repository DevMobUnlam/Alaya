package com.devmob.alaya.domain.model

import com.google.gson.annotations.SerializedName

data class EmotionIAPrompt(
    @SerializedName("name")
    val name: String = "",
    @SerializedName("intensity")
    val intensity: String = ""

)
