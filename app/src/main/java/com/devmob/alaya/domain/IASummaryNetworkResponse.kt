package com.devmob.alaya.domain

import com.google.gson.annotations.SerializedName

data class IASummaryNetworkResponse(
    @SerializedName("additional_comments")
    val additionalComments: String,
//    @SerializedName("journal_entries")
//    val journalEntries: List<String>,
)
