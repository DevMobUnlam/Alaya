package com.devmob.alaya.domain.model.util

import com.devmob.alaya.domain.model.BodySensationDB
import com.devmob.alaya.domain.model.CrisisBodySensation
import com.devmob.alaya.domain.model.CrisisDetails
import com.devmob.alaya.domain.model.CrisisDetailsDB
import com.devmob.alaya.domain.model.CrisisEmotion
import com.devmob.alaya.domain.model.CrisisEmotionDB

internal fun CrisisDetails.toDB(): CrisisDetailsDB {
    return CrisisDetailsDB(
        start = this.crisisTimeDetails.startTime,
        end = this.crisisTimeDetails.endTime,
        place = this.placeList.firstOrNull()?.name,
        bodySensations = this.bodySensationList.map { it.toDB() },
        tools = this.toolList.map { it.name },
        emotions = this.emotionList.map { it.toDB() },
        notes = this.notes,
        completed = this.completed
    )
}

internal fun CrisisEmotion.toDB() = CrisisEmotionDB(this.name, this.intensity)

internal fun CrisisBodySensation.toDB() : BodySensationDB = BodySensationDB(this.name, this.intensity)
