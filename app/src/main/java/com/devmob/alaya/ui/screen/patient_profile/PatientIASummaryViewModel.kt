package com.devmob.alaya.ui.screen.patient_profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devmob.alaya.domain.GetIASummaryUseCase
import com.google.ai.client.generativeai.GenerativeModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

@HiltViewModel
class PatientIASummaryViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getIASummaryUseCase: GetIASummaryUseCase
): ViewModel() {


    private val _uiState: MutableStateFlow<IASummaryUIState> = MutableStateFlow(IASummaryUIState.Initial)
    val uiState: StateFlow<IASummaryUIState> = _uiState.asStateFlow()

    private val patientId: String = savedStateHandle["patientID"]?:""
    private lateinit var job: Job


    init {
        summarize()
    }

    private fun summarize(){

        _uiState.value = IASummaryUIState.Loading


        job = viewModelScope.launch {


            try{

                if(patientId == ""){
                    _uiState.update { IASummaryUIState.Error("El paciente no existe") }
                }else{
                    val response = getIASummaryUseCase(patientId = patientId)

                    response.collect { outputContent ->
                       _uiState.update { outputContent }
                    }


                }
            } catch (e:Exception){
                _uiState.update{ IASummaryUIState.Error(e.message ?: "") }
            }
        }


    }

    fun onRetryClick(){
        job.cancel()
        summarize()
    }



}