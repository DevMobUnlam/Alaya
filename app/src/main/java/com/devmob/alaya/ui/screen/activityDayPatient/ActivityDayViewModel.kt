package com.devmob.alaya.ui.screen.activityDay

import ActivityDayUIState
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devmob.alaya.domain.PatientDailyActivitiesUseCases
import com.devmob.alaya.domain.model.DailyActivity
import com.devmob.alaya.domain.model.FirebaseResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActivityDayViewModel @Inject constructor(
    private val patientDailyActivitiesUseCases: PatientDailyActivitiesUseCases
): ViewModel() {

    private val _uiState = MutableStateFlow(ActivityDayUIState())
    val uiState = _uiState.asStateFlow()

    var focusedActivity by mutableStateOf(DailyActivity())
    var shouldShowDescriptionModal by mutableStateOf(false)
    var shouldShowPostActivityModal by mutableStateOf(false)

    fun loadDailyActivities(){
        viewModelScope.launch(Dispatchers.IO){
            Log.d("ActivityDayViewModel", "scope launched called")
            try {
                patientDailyActivitiesUseCases.getPatientDailyActivitiesUseCase.invoke().collect{ list ->
                    if(!list.isNullOrEmpty()){
                        _uiState.update {
                            ActivityDayUIState(activityList = list, isLoading = false)
                        }
                    }else{
                        _uiState.update {
                            ActivityDayUIState(activityList = emptyList(), isLoading = false)
                        }
                    }

                }
            } catch (e: Exception) {
                Log.e("ActivityDayViewModel", e.message?:"")
            }
        }

    }

    fun onActivityCheckClick(dailyActivity: DailyActivity){
        val updatedProgress = dailyActivity.currentProgress+1
        if(!dailyActivity.isDone){
            focusedActivity = dailyActivity
            viewModelScope.launch(Dispatchers.IO) {
                val result = patientDailyActivitiesUseCases.changeDailyActivityStatusUseCase(true, dailyActivity, updatedProgress)
                when(result){
                    is FirebaseResult.Success -> {
                        Log.i("ChangeActivityStatus", "Status was updated succesfully")
                        showPostActivityModal()
                    }
                    else -> {Log.e("ChangeActivityStatus",
                        (result as FirebaseResult.Error).t?.message ?:"")}
                }
            }

//            _uiState.update {
//                    val updatedList = it.activityList.toMutableList()
//                    updatedList[index] = dailyActivity.copy(isDone = true)
//                ActivityDayUIState(activityList = updatedList.toList(), isLoading = false)
//            }
        }

    }

    fun showActivityDescriptionModal(activity: DailyActivity){
        focusedActivity = activity
        shouldShowDescriptionModal = true
    }

    fun hideActivityDescriptionModal(){
        shouldShowDescriptionModal = false
    }

    fun onSavePostActivityComment(comment: String){
        viewModelScope.launch(Dispatchers.IO){
            val result = patientDailyActivitiesUseCases.savePostActivityCommentUseCase(comments = comment, activityId = focusedActivity.id)
            when(result){
                is FirebaseResult.Success -> {
                    Log.i("PostActivityModal", "Comment was updated succesfully")
                    hidePostActivityModal()
                }
                else -> {Log.e("PostActivityModal",
                    (result as FirebaseResult.Error).t?.message ?:"")
                    hidePostActivityModal()
                }
            }
        }
    }

    private fun showPostActivityModal(){
        shouldShowPostActivityModal = true
    }

    fun hidePostActivityModal(){
        shouldShowPostActivityModal = false
    }

    fun onMicrophoneClick(){
        TODO()
    }

    override fun onCleared() {
        viewModelScope.cancel()
        super.onCleared()
    }


}