package com.devmob.alaya.data.mapper

import com.devmob.alaya.domain.model.FirebaseResult

fun <T> Result<T>.toResponseFirebase(): FirebaseResult {
     return when {
         this.isSuccess -> {
             FirebaseResult.Success
         }

         this.isFailure -> FirebaseResult.Error(checkNotNull(this.exceptionOrNull()))
         else -> FirebaseResult.Error(null)
     }
 }