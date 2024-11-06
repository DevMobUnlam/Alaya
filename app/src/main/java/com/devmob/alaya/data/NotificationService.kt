package com.devmob.alaya.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class NotificationService {
    private val retrofit = RetrofitHelper.getRetrofit()

    suspend fun sendNotification(notificationInvitation: NotificationInvitation): Response<Unit> {
        return withContext(Dispatchers.IO) {
            retrofit.create(NotificationApiClient::class.java).sendNotification(
                apiKey = "Basic ODJjNGNhNmItYzA2YS00OWQ2LTljMjctZDMwYzEyNmU4NWFk", //No deberíamos subir a github esta apikey.
                body = notificationInvitation
            )
        }
    }
}