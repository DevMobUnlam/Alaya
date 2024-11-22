package com.devmob.alaya.domain

import com.devmob.alaya.domain.model.FirebaseResult

class SavePostActivityCommentUseCase(
    private val repository: DailyActivityRepository
) {
    suspend operator fun invoke(activityId: String, comments: String): FirebaseResult {
        return repository.savePostActivityComments(activityId,comments)
    }
}