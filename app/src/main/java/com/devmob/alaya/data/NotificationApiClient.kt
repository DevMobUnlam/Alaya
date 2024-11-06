package com.devmob.alaya.data


import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface NotificationApiClient {
    @POST("/notifications")
    suspend fun sendNotification(@Header("Authorization") apiKey: String, @Body body:NotificationInvitation): Response<Unit>
}