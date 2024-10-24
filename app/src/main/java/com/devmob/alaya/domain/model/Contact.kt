package com.devmob.alaya.domain.model

import java.io.Serializable

data class Contact(
    val contactId: String,
    val name: String,
    val numberPhone: String,
    val photo: String? = "",
) : Serializable{
    constructor() : this("", "", "", "")
}
