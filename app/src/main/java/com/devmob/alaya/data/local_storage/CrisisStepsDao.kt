package com.devmob.alaya.data.local_storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.devmob.alaya.domain.model.OptionTreatment

@Dao
interface CrisisStepsDao {

    @Insert (onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCrisisStep(crisisStep: OptionTreatment)

    @Query("SELECT * FROM crisis_steps")
    suspend fun getCrisisSteps(): List<OptionTreatment>
}
