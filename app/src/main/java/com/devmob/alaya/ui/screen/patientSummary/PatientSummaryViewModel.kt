package com.devmob.alaya.ui.screen.patientSummary

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devmob.alaya.data.CrisisRepositoryImpl
import com.devmob.alaya.domain.GetIASummaryUseCase
import com.google.ai.client.generativeai.GenerativeModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PatientSummaryViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getIASummaryUseCase: GetIASummaryUseCase
): ViewModel() {


    private val _uiState: MutableStateFlow<SummaryUIState> = MutableStateFlow(SummaryUIState.Initial)
    val uiState: StateFlow<SummaryUIState> = _uiState.asStateFlow()

    init{
        //summarize(savedStateHandle["patientId"] ?:"")
        // TODO() Implementar nav argument con id de paciente
        summarize("maurojose029@gmail.com")
     }

    private fun summarize(patientId: String){
        _uiState.value = SummaryUIState.Loading

        val instructions = "-Generar un resumen a partir del siguiente JSON,emociones sentidas,comentarios adicionales, para poder realizar una lectura posterior\n" +
                "- El resumen deberia tener 2 parrafos como maximo, con informacion comprimida\n" +
                "- El resumen debe abarcar la semana entera, no es necesario resumir cada dia individualmente\n" +
                "- Usar el nombre de la persona de la cual se esta resumiendo su semana."

        val generativeModel = GenerativeModel(
            modelName = "gemini-1.5-flash",
            apiKey = "AIzaSyBFGQTD_CB_Yoog-EvnDHkP1RjpFYAQZDs")

        viewModelScope.launch {
            try{
                val response = getIASummaryUseCase(instructions = instructions, generativeModel = generativeModel, patientId = patientId)

                response.collect{ outputContent ->
                    if(_uiState.value is SummaryUIState.Success){
                        _uiState.value = SummaryUIState.Loading
                    }
                    _uiState.value = SummaryUIState.Success(outputContent)
                }

            }catch(e:Exception){
                _uiState.value = SummaryUIState.Error(e.localizedMessage?: "")
            }
        }

    }

}