package com.devmob.alaya.data

import com.google.gson.annotations.SerializedName

data class IASummaryNetworkResponse(
    @SerializedName("additional_comments")
    val additionalComments: String,
    @SerializedName("place")
    val place: String,
    @SerializedName("emotions")
    val emotions: List<String>,
    @SerializedName("sensations")
    val sensations: List<String>,
)
