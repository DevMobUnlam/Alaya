package com.devmob.alaya.data.local_storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.devmob.alaya.domain.model.OptionTreatment

@Dao
interface CrisisStepsDao {

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCrisisStep(crisisStep: OptionTreatment)

    @Query("DELETE FROM crisis_steps")
    suspend fun deleteAllCrisisSteps()

    @Query("SELECT * FROM crisis_steps")
    suspend fun getCrisisSteps(): List<OptionTreatment>
}
