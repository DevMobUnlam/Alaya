package com.devmob.alaya.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devmob.alaya.domain.model.FeedbackType
import kotlinx.coroutines.launch

class FeedbackViewModel : ViewModel() {
    private var _feedbackType = FeedbackType.Felicitaciones
    val feedbackType: FeedbackType
        get() = _feedbackType

    fun setFeedbackType(type: FeedbackType) {
        _feedbackType = type
    }

    fun registerEpisode() {
        viewModelScope.launch {
        }
    }

    fun registerLater() {
        viewModelScope.launch {
        }
    }

    fun goToSupportNetwork() {
        viewModelScope.launch {
        }
    }

    fun goToHome() {
        viewModelScope.launch {
        }
    }
}
