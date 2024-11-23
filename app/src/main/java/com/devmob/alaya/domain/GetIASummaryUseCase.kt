package com.devmob.alaya.domain

import android.util.Log
import com.devmob.alaya.data.mapper.toIASummaryModel
import com.devmob.alaya.domain.model.IASummaryText
import com.devmob.alaya.ui.screen.patient_profile.IASummaryUIState
import com.google.ai.client.generativeai.GenerativeModel
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetIASummaryUseCase @Inject constructor(
    private val gson: Gson,
    private val generativeModel: GenerativeModel,
    private val crisisRepository: CrisisRepository,
    private val getUserRepository: GetUserRepository,
) {

    operator fun invoke(
        patientId: String,
    ): Flow<IASummaryUIState> {
        return flow {
            try {
                val patientName = getUserRepository.getUser(patientId)?.name
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
                            "-Generar un resumen a partir del siguiente JSON\n" +
                                    "- Usar el nombre de la persona. \n" +
                                    "- La respuesta debe ser generada siguiendo el esquema de a continuacion: \n" +
                                    "{'timeAndPlace': (en este campo se escribe un resumen pequeño de en que lugares ocurrieron, en que dias de la semana ocurrieron (lunes, martes, etc.) y los momentos del día en que ocurrieron, no la hora. Este campo debe comensar informando la cantidad de eventos." +
                                    "\n" +
                                    "'details': (en este campo enumerar todas las emociones y sensaciones corporales sentidas, con su respectiva intensidad. Tambien, las herramientas que mas uso para sentirse mejor. No mensionar fechas. " +
                                    "\n" +
                                    "'extra': (en este campo va un resumen con comentarios adicionales)" + "\n" +
                                    "}" + "Texto generado debe comenzar solo con { y terminar solo con }" + "El tipo de intensidad debe ser traducido"

                        val prompt = "$instructions \n $json"
                        val generatedResponse = generativeModel.generateContent(prompt).text
                        val outputContent =
                            gson.fromJson(generatedResponse, IASummaryText::class.java)
                        emit(IASummaryUIState.Success(outputContent))
                    }
                }
            } catch (e: Exception) {
                Log.e("GetIASummaryUseCase", e.message ?: "")
                emit(IASummaryUIState.Error(e.message ?: ""))
            }
        }
    }
}
