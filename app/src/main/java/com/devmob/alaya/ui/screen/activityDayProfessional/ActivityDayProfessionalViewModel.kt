package com.devmob.alaya.ui.screen.activityDayProfessional

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devmob.alaya.domain.ProfessionalDailyActivitiesUseCases
import com.devmob.alaya.domain.model.DailyActivity
import com.devmob.alaya.domain.model.FirebaseResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActivityDayProfessionalViewModel @Inject constructor(
    private val useCases: ProfessionalDailyActivitiesUseCases
): ViewModel() {

    private var _uiState = MutableStateFlow(ActivityDayProfessionalUIState(isLoading = true))
    val uiState = _uiState.asStateFlow()

    var saveActivityResult = mutableStateOf<Boolean?>(null)
        private set

    var focusedActivity by mutableStateOf(DailyActivity())
        private set

    private var job: Job? = null


    fun getDailyActivities(patientID: String){
        job = viewModelScope.launch {
            try {
                useCases.getDailyActivitiesUseCase(patientID).collect{ list ->
                    if (list != null) {
                        if(list.isNotEmpty()){
                            _uiState.update { it.copy(activityList = list, isLoading = false, isError = false) }
                        }else{
                            _uiState.update { it.copy(isLoading = false, isError = false) }
                        }
                    }else{
                        _uiState.update { it.copy(isLoading = false, isError = true) }
                    }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, isError = true) }

            }
        }
    }

    fun onCreateActivity(){
        saveActivityResult.value = null
        focusedActivity = DailyActivity()
        _uiState.update { it.copy(isCreating = true)}
    }

    fun onSaveActivity(patientID: String,title: String, description: String, maxProgress:Int){
        viewModelScope.launch {
            try {
                val result: FirebaseResult = if(!_uiState.value.isCreating){
                    editActivity(patientID, title, description, maxProgress)
                }else{
                    createActivity(patientID, title, description, maxProgress)
                }

                if(result == FirebaseResult.Success){
                    saveActivityResult.value = true
                    _uiState.update { it.copy(isCreating = false) }
                }else{
                    saveActivityResult.value = false
                }
            } catch (e: Exception) {
                this@ActivityDayProfessionalViewModel.saveActivityResult.value = false
                this.coroutineContext.cancel()
            }
        }

    }

    fun onEditActivity(activity: DailyActivity){
        saveActivityResult.value = null
        focusedActivity = activity
        _uiState.update { it.copy(isCreating = false) }

    }


    private suspend fun createActivity(patientID: String,title: String, description: String, maxProgress:Int): FirebaseResult{
        val activity = DailyActivity(
            title = title,
            description = description,
            maxProgress = maxProgress,
        )
        return useCases.createDailyActivityUseCase(patientID, activity)
    }

    private suspend fun editActivity(patientID: String, title: String, description: String, maxProgress:Int): FirebaseResult{
        val dailyActivity = DailyActivity(id = focusedActivity.id,title = title, description = description, maxProgress = maxProgress)
        return useCases.editDailyActivityUseCase(patientID,dailyActivity)
    }

    fun onDismiss(){
        _uiState.update {it.copy(isCreating = false) }
    }

    fun cancelJob(){
        job?.cancel()
    }

}

