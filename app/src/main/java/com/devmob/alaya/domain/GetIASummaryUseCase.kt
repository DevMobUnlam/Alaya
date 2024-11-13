package com.devmob.alaya.domain

import android.content.Context
import android.icu.util.Calendar
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import com.devmob.alaya.R
import com.devmob.alaya.data.mapper.toIASummaryModel
import com.devmob.alaya.domain.model.IASummaryText
import com.devmob.alaya.ui.screen.patient_profile.IASummaryUIState
import com.devmob.alaya.utils.toCalendar
import com.google.ai.client.generativeai.GenerativeModel
import com.google.gson.Gson
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.timeout
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds


class GetIASummaryUseCase @Inject constructor(
    private val gson: Gson,
    private val generativeModel: GenerativeModel,
    private val crisisRepository: CrisisRepository,
    private val getUserRepository: GetUserRepository,
    ) {

    @OptIn(FlowPreview::class)
    suspend operator fun invoke(
         patientId: String,
): Flow<IASummaryUIState> {

        return flow {


            try {
                val patientName =  getUserRepository.getUser(patientId)?.name


                crisisRepository.getRegisters(patientId).collect { crisisDetailsDBList ->
                        val mappedList = crisisDetailsDBList?.map { it.toIASummaryModel() }

                        if (mappedList?.isEmpty() == true) {
                            emit(IASummaryUIState.EmptyContent)
                        } else {
                            val json =
                                gson.toJson(
                                    IASummaryPrompt(
                                        patientName = patientName ?: "",
                                        input = mappedList ?: emptyList(),
                                    ),
                                )

                            val instructions =
                                "-Generar un resumen (que no omita ningun detalle a partir de la informacion recibida) a partir del siguiente JSON\n" +
                                    "- Usar el nombre de la persona. \n" +
                                    "- La respuesta debe ser generada siguiendo el esquema de a continuacion: \n" +
                                    "{'timeAndPlace': (en este campo se escribe un resumen pequeño de en que lugares ocurrieron, y en que dias de la semana ocurrieron (lunes, martes, etc.)" +
                                    "\n" +
                                    "'details': (en este campo iria un resumen con emociones y sensaciones corporales sentidas, con su respectiva intensidad. Tambien, las herramientas que uso para sentirse mejor" +
                                    "\n" +
                                    "'extra': (en este campo va un resumen con comentarios adicionales)" + "\n" +
                                    "}" + "La respuesta generada solo deberia tener el esquema json, ningun caracter de mas o espacios"+ "El tipo de intensidad debe ser traducido"

                            val prompt = "$instructions \n $json"
                            val generatedResponse = generativeModel.generateContent(prompt).text
                            val outputContent =
                                gson.fromJson(generatedResponse, IASummaryText::class.java)
                            emit(IASummaryUIState.Success(outputContent))
                        }
                    }

            } catch (e: Exception) {
                Log.e("GetIASummaryUseCase", e.message?: "")
                emit(IASummaryUIState.Error(e.message?: ""))
            }
        }
    }
}

