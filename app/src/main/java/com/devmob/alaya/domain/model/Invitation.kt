package com.devmob.alaya.domain.model

import java.io.Serializable

data class Invitation(
    val email: String,
    val status: InvitationStatus
) :
    Serializable {
    constructor() : this("", InvitationStatus.NONE)
}

enum class InvitationStatus {
    PENDING,
    ACCEPTED,
    REJECTED,
    NONE
}
