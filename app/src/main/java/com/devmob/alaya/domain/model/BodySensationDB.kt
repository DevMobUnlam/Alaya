package com.devmob.alaya.domain.model

data class BodySensationDB(
    val name: String = "",
    val intensity: Intensity = Intensity.LOW
)
{
    constructor() : this("", Intensity.LOW)
}
