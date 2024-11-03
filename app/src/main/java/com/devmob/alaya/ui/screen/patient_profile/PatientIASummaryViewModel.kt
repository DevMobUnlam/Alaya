package com.devmob.alaya.ui.screen.patient_profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devmob.alaya.domain.GetIASummaryUseCase
import com.google.ai.client.generativeai.GenerativeModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PatientIASummaryViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getIASummaryUseCase: GetIASummaryUseCase
): ViewModel() {


    private val _uiState: MutableStateFlow<IASummaryUIState> = MutableStateFlow(IASummaryUIState.Initial)
    val uiState: StateFlow<IASummaryUIState> = _uiState.asStateFlow()


    private val patientId: String = savedStateHandle["patientID"]?:""


    init {
        summarize()
    }

    private fun summarize(){

        _uiState.value = IASummaryUIState.Loading


        val instructions = "-Generar un resumen a partir del siguiente JSON,emociones sentidas,comentarios adicionales, para poder realizar una lectura posterior\n" +
                "- El resumen deberia tener 2 parrafos como maximo, con informacion comprimida\n" +
                "- El resumen debe abarcar la semana entera, no es necesario resumir cada dia individualmente\n" +
                "- Usar el nombre de la persona de la cual se esta resumiendo su semana."


        viewModelScope.launch {


            try{

                if(patientId == ""){
                    _uiState.update { IASummaryUIState.Error("The patient doesn't exist") }
                }else{
                    val response = getIASummaryUseCase(instructions = instructions, patientId = patientId, onRegisterUpdate = {
                        _uiState.value = IASummaryUIState.Loading
                    }
                    )

                    response.collect{ outputContent ->

                        if(outputContent.isNotEmpty()){
                            _uiState.update { IASummaryUIState.Success(outputContent) }

                        }else{
                            _uiState.update{ IASummaryUIState.Success("No hay contenido para resumir") }
                        }
                    }
                }


            }catch(e:Exception){
                _uiState.update{ IASummaryUIState.Error(e.localizedMessage ?: "") }
            }
        }


    }

    fun onRetryClick(){
        summarize()
    }


}