package com.devmob.alaya.domain

data class PatientDailyActivitiesUseCases(
    val changeDailyActivityStatusUseCase: ChangeDailyActivityStatusUseCase,
    val getPatientDailyActivitiesUseCase: GetPatientDailyActivitiesUseCase,
    val savePostActivityCommentUseCase: SavePostActivityCommentUseCase
)
