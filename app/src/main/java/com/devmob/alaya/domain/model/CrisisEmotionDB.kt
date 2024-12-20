package com.devmob.alaya.domain.model

data class CrisisEmotionDB(
    val name: String = "",
    val intensity: Intensity = Intensity.LOW
)
{
    constructor() : this("", Intensity.LOW)
}
