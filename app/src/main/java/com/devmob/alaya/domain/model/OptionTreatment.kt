package com.devmob.alaya.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.devmob.alaya.R

@Entity(tableName = "crisis_steps", primaryKeys = ["title", "description", "imageUri"])
data class OptionTreatment(
    val title: String,
    val description: String,
    val imageUri: String = "",
    val imageResId: Int = R.drawable.logounologin,
    val animationRes: Int? = null
) {
    constructor() : this("","", "")
}
