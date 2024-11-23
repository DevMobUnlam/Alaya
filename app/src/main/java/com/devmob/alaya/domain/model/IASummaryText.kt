package com.devmob.alaya.domain.model

import com.google.gson.annotations.SerializedName

data class IASummaryText(
    @SerializedName("timeAndPlace")
    val timeAndPlace: String = "",
    @SerializedName("details")
    val details: String = "",
    @SerializedName("extra")
    val extra: String = ""


)
