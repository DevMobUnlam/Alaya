package com.devmob.alaya.utils

import android.content.Context
import coil.ImageLoader
import coil.annotation.ExperimentalCoilApi
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.devmob.alaya.data.local_storage.CrisisStepsDao
import com.devmob.alaya.domain.GetCrisisTreatmentUseCase
import com.devmob.alaya.domain.model.OptionTreatment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalCoilApi::class)
class CrisisStepsManager(
    private val crisisStepsDao: CrisisStepsDao,
    private val getCrisisTreatmentUseCase: GetCrisisTreatmentUseCase,
    private val context: Context,
) {

    private var crisisStepsRemoteDB: List<OptionTreatment>? = null
    private var crisisStepsLocalDB: List<OptionTreatment>? = null

    suspend fun updateCrisisSteps(patientEmail: String) {
        if (shouldUpdateCrisisStepsLocalDB(patientEmail)) {
            crisisStepsDao.deleteAllCrisisSteps()
            deleteImagesFromCache()
            crisisStepsRemoteDB?.let { steps ->
                steps.forEach { step ->
                    val localPath = downloadAndCacheImage(step.imageUri)
                    val updatedStep = step.copy(imageLocalPath = localPath)
                    crisisStepsDao.insertCrisisStep(updatedStep)
                }
            }
        }
    }

    private suspend fun downloadAndCacheImage(imageUrl: String): String? {
        val imageLoader = ImageLoader.Builder(context).build()

        val request = ImageRequest.Builder(context)
            .data(imageUrl)
            .diskCacheKey(imageUrl)
            .diskCachePolicy(CachePolicy.ENABLED)
            .build()

        return withContext(Dispatchers.IO) {
            val result = imageLoader.execute(request)
            if (result is SuccessResult) {
                val diskCache = imageLoader.diskCache
                val cacheFile = diskCache?.get(imageUrl)?.data?.toFile()
                cacheFile?.absolutePath
            } else {
                null
            }
        }
    }

    private suspend fun shouldUpdateCrisisStepsLocalDB(patientEmail: String): Boolean {
        crisisStepsRemoteDB = getCrisisTreatmentUseCase.getDataFromRemoteDatabase(patientEmail)
        crisisStepsLocalDB = crisisStepsDao.getCrisisSteps()

        return (crisisStepsRemoteDB != crisisStepsLocalDB) || crisisStepsLocalDB.isNullOrEmpty()
    }

    private fun deleteImagesFromCache() {
        val imageLoader = ImageLoader.Builder(context).build()
        val diskCache = imageLoader.diskCache
        diskCache?.clear()
    }
}
