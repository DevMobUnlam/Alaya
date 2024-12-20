package com.devmob.alaya.domain

import com.devmob.alaya.data.IASummaryNetworkResponse
import com.google.gson.annotations.SerializedName

data class IASummaryPrompt(
    @SerializedName("patient_name")
    val patientName: String = "",
    @SerializedName("input")
    val input: List<IASummaryNetworkResponse>,
)
