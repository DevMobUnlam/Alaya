package com.devmob.alaya.data.mapper

import com.devmob.alaya.data.DailyActivityBody
import com.devmob.alaya.data.DailyActivityNetwork
import com.devmob.alaya.data.DailyActivityPost
import com.devmob.alaya.domain.model.DailyActivity
import com.google.firebase.firestore.QueryDocumentSnapshot
import java.util.Date

fun DailyActivityNetwork.toDomain(completedToday: Boolean) : DailyActivity{
    val isFinished: Boolean = completedToday || this.currentProgress == this.maxProgress

    return DailyActivity(
        id = this.id,
        title = this.title,
        description = this.description,
        currentProgress = this.currentProgress,
        maxProgress = this.maxProgress,
        isDone = isFinished
        )
}

fun QueryDocumentSnapshot.toData() : DailyActivityNetwork {
    return DailyActivityNetwork(
        id = this.id,
        title = this.getString("title")?:"",
        description = this.getString("description")?:"",
        currentProgress = this.get("currentProgress").toString().toIntOrNull()?:0,
        maxProgress = this.get("maxProgress").toString().toIntOrNull()?:0,
        lastCompleted = this.getDate("lastCompleted")
    )
}

fun DailyActivity.toNetwork(): DailyActivityBody {
    return DailyActivityBody(
        title = this.title,
        description = this.description,
        maxProgress = this.maxProgress,
        currentProgress = this.currentProgress,
        id = this.id
    )
}
    fun DailyActivityBody.toNetworkPost(): DailyActivityPost{
        return DailyActivityPost(
            title = this.title,
            description = this.description,
            maxProgress = this.maxProgress,
        )
}