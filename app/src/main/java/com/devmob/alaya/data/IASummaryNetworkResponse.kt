package com.devmob.alaya.data

import com.devmob.alaya.domain.model.BodySensationIAPrompt
import com.devmob.alaya.domain.model.EmotionIAPrompt
import com.devmob.alaya.domain.model.TimeAndPlaceIAPrompt
import com.devmob.alaya.domain.model.ToolsIAPrompt
import com.google.gson.annotations.SerializedName

data class IASummaryNetworkResponse(
    @SerializedName("additional_comments")
    val additionalComments: String,
    @SerializedName("timeAndPlace")
    val timeAndPlace: TimeAndPlaceIAPrompt,
    @SerializedName("emotions")
    val emotions: List<EmotionIAPrompt>,
    @SerializedName("sensations")
    val sensations: List<BodySensationIAPrompt>,
    @SerializedName("tools")
    val tools: List<ToolsIAPrompt>

)
