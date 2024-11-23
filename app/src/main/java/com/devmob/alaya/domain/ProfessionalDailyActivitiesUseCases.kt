package com.devmob.alaya.domain

data class ProfessionalDailyActivitiesUseCases(
    val getDailyActivitiesUseCase: GetProfessionalDailyActivitiesUseCase,
    val createDailyActivityUseCase: CreateDailyActivityUseCase,
    val editDailyActivityUseCase: EditDailyActivityUseCase,
)
