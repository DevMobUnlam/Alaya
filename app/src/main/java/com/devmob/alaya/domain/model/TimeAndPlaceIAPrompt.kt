package com.devmob.alaya.domain.model

import com.google.gson.annotations.SerializedName
import java.util.Date

data class TimeAndPlaceIAPrompt(
    @SerializedName("place")
    val place: String? = "",
    @SerializedName("day")
    val day: Date? = Date()

)
