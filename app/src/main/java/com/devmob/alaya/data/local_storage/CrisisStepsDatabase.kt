package com.devmob.alaya.data.local_storage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.devmob.alaya.domain.model.OptionTreatment

@Database(entities = [OptionTreatment::class], version = 3)
abstract class CrisisStepsDatabase : RoomDatabase() {
    abstract fun crisisStepsDao(): CrisisStepsDao

    companion object {
        private const val DATABASE_NAME = "crisis_steps_db"

        private var INSTANCE: CrisisStepsDatabase? = null

        fun getDataBase(context: Context): CrisisStepsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, CrisisStepsDatabase::class.java, DATABASE_NAME
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}