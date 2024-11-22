package com.devmob.alaya.data.mapper

import com.devmob.alaya.data.DailyActivityNetwork
import com.devmob.alaya.domain.model.DailyActivity
import com.google.firebase.firestore.QueryDocumentSnapshot
import java.util.Date

fun DailyActivityNetwork.toDomain(completedToday: Boolean) : DailyActivity{
    var isFinished = false
    if(completedToday || this.currentProgress == this.maxProgress){
        isFinished = true
    }else{
        isFinished = false
    }

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