package com.devmob.alaya.data

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {

    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.onesignal.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okhttpClient())
            .build()
    }
    private val logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    private fun okhttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            //.addInterceptor(AuthInterceptor(context)) //TODO: Agregar interceptor para la autenticación
            .addNetworkInterceptor(logging)
            .build()
    }


}
