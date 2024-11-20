package com.devmob.alaya.ui.screen.activityDay

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devmob.alaya.domain.model.DailyActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ActivityDayViewModel @Inject constructor(): ViewModel() {

    private val _uiState = MutableStateFlow(ActivityDayUIState())
    val uiState = _uiState.asStateFlow()

    fun loadDailyActivities(){
        // TODO VIEWMODEL SCOPE, LLAMADA A CASO DE USO, ACTUALIZACION DE UISTATE

//        _uiState.update {
//            ActivityDayUIState(activityList = dailyActivities, isLoading = false)
//        }
    }

    fun onActivityCheckClick(dailyActivity: DailyActivity, index: Int){
        //TODO CASO DE USO PARA COMPLETAR TAREA
        if(!dailyActivity.isChecked){
            _uiState.update {
                val updatedList = it.activityList.toMutableList()
                updatedList[index] = dailyActivity.copy(isChecked = true)
                ActivityDayUIState(activityList = updatedList.toList(), isLoading = false)
            }
        }

    }

    override fun onCleared() {
        viewModelScope.cancel()
        super.onCleared()
    }


}