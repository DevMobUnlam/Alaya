package com.devmob.alaya.domain

import android.util.Log
import com.devmob.alaya.data.mapper.toIASummaryModel
import com.google.ai.client.generativeai.GenerativeModel
import com.google.gson.Gson

class GetIASummaryUseCase {

    suspend operator fun invoke(
        instructions: String,
        generativeModel: GenerativeModel,
        patientId: String,
        crisisRepository: CrisisRepository): String? {

        val gson = Gson()

        // TODO() Obtener Nombre de Paciente de base de datos
        // TODO() Inyectar repositorio con hilt

        val crisisRegisters = crisisRepository.getRegisters(patientId)?.map { it.toIASummaryModel("Mauro") }


        val json = gson.toJson(crisisRegisters)

        Log.v("Json Prompt", json)

        val prompt = "$instructions \n $json"

        return generativeModel.generateContent(prompt).text

    }
}