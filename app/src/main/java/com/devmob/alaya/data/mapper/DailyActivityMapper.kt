package com.devmob.alaya.data.mapper

import com.devmob.alaya.domain.model.DailyActivity
import com.google.firebase.firestore.QueryDocumentSnapshot

fun QueryDocumentSnapshot.toDomain() : DailyActivity{
    return DailyActivity(
        id = this.id,
        title = this.getString("title")?:"",
        description = this.getString("description")?:"",
        currentProgress = this.get("currentProgress").toString().toIntOrNull()?:0,
        maxProgress = this.get("maxProgress").toString().toIntOrNull()?:0,
        isDone = this.getBoolean("isDone")?:false
        )
}