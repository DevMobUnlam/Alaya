package com.devmob.alaya.domain.model

import java.io.Serializable

data class Professional (
    val email: String,
    val name: String,
    val surname: String,
    val phone: String,
    val nextSession: String? = null,
): Serializable {
    constructor() : this("", "", "", "", "")
}
