package com.devmob.alaya.domain

import com.google.ai.client.generativeai.GenerativeModel

class GetIASummaryUseCase {

    suspend operator fun invoke(
        instructions: String,
        generativeModel: GenerativeModel,
        patientId: String): String? {

        val json = ""

        val prompt = "$instructions \n $json"

        return generativeModel.generateContent(prompt).text

    }
}