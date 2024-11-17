package com.devmob.alaya.utils

import android.content.Context
import android.net.Uri
import android.util.Log
import coil.ImageLoader
import coil.annotation.ExperimentalCoilApi
import coil.imageLoader
import coil.request.ImageRequest
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
            withContext(Dispatchers.IO) {
                crisisStepsDao.deleteAllCrisisSteps()
                deleteImagesFromCache()
            }
            crisisStepsRemoteDB?.let { steps ->
                steps.forEach { step ->
                    preloading(Uri.parse(step.imageUri))
                    crisisStepsDao.insertCrisisStep(step)
                }
            }
        }
    }

    private fun preloading(uri: Uri?) {
        val request = ImageRequest.Builder(context)
            .data(uri)
            .crossfade(true)
            .listener(
                onStart = { Log.d("Preloading", "Starting to preload $uri") },
                onSuccess = { _, _ -> Log.d("Preloading", "Successfully preloaded $uri") },
                onError = { _, throwable ->
                    Log.e(
                        "Preloading",
                        "Error preloading $uri $throwable"
                    )
                }
            )
            .build()

        context.imageLoader.enqueue(request)
    }

    private suspend fun shouldUpdateCrisisStepsLocalDB(patientEmail: String): Boolean {
        crisisStepsRemoteDB = getCrisisTreatmentUseCase.getDataFromRemoteDatabase(patientEmail)
        crisisStepsLocalDB = crisisStepsDao.getCrisisSteps()

        val remoteData = crisisStepsRemoteDB?.map { Triple(it.title, it.description, it.imageUri) }
        val localData = crisisStepsLocalDB?.map { Triple(it.title, it.description, it.imageUri) }

        return (remoteData != localData) || localData.isNullOrEmpty()
    }

    private fun deleteImagesFromCache() {
        val imageLoader = ImageLoader.Builder(context).build()
        imageLoader.memoryCache?.clear()
        imageLoader.diskCache?.clear()
    }
}
