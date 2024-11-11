package com.devmob.alaya.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.devmob.alaya.R

@Entity(tableName = "crisis_steps")
data class OptionTreatment(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val imageUri: String,
    val imageResId: Int = R.drawable.logounologin,
    @ColumnInfo(name = "image_local_path") val imageLocalPath: String? = null
) {
    constructor() : this(0,"","", "")
}
