package com.devmob.alaya.domain.model

sealed class FirebaseResult {

    data class Error(val t: Throwable?) : FirebaseResult()
    data object Success : FirebaseResult()
}
