package com.devmob.alaya.ui.screen.crisis_handling

import android.util.Log
import androidx.compose.runtime.MutableState
import android.content.Context
import android.media.MediaPlayer

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewModelScope
import com.devmob.alaya.data.FirebaseClient
import com.devmob.alaya.domain.GetCrisisTreatmentUseCase
import com.devmob.alaya.domain.SaveCrisisTreatmentUseCase
import com.devmob.alaya.domain.model.OptionTreatment
import com.devmob.alaya.domain.model.StepCrisis
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.launch
import kotlin.reflect.jvm.internal.impl.load.java.structure.JavaClass

class CrisisHandlingViewModel(private val getCrisisTreatmentUseCase: GetCrisisTreatmentUseCase) :
    ViewModel() {

    val currentUser = FirebaseClient().auth.currentUser
    var steps by mutableStateOf<List<StepCrisis>>(emptyList())
    var optionTreatmentsList by mutableStateOf<List<OptionTreatment>?>(null)
    var currentStepIndex by mutableIntStateOf(0)
    var shouldShowModal by mutableStateOf(false)
    var shouldShowExitModal by mutableStateOf(false)
    var isPlaying by mutableStateOf(false)
    private var player: MediaPlayer? = null

    private val _loading = mutableStateOf(true)
    val loading: MutableState<Boolean>
        get() = _loading

    val currentStep: StepCrisis?
        get() = if (steps.isNotEmpty()) {
            steps[currentStepIndex]
        } else null


    init {
        fetchCrisisSteps()
    }

    fun fetchCrisisSteps() {
        var stepCrisisList: List<StepCrisis>
        viewModelScope.launch {
            _loading.value = true
            try {
                // Obtén los tratamientos del terapeuta
                optionTreatmentsList = currentUser?.email?.let { getCrisisTreatmentUseCase(it) }

                // Si hay tratamientos por terapeuta
                if (!optionTreatmentsList.isNullOrEmpty()) {
                    stepCrisisList = optionTreatmentsList!!.map { option ->
                        StepCrisis(
                            title = option.title,
                            description = option.description,
                            image = option.imageUri
                        )
                    }
                    steps = stepCrisisList
                } else {
                    // Si no hay tratamiento por terapeuta, carga los pasos predeterminados
                    steps = listOf(
                        StepCrisis(
                            "Controlar la respiración",
                            "Poner una mano en el pecho y la otra en el estómago para tomar aire y soltarlo lentamente",
                            "image_step_1"
                        ),
                        StepCrisis(
                            "Imaginación guiada",
                            "Cerrar los ojos y pensar en un lugar tranquilo, prestando atención a todos los sentidos del ambiente que te rodea",
                            "image_step_2"
                        ),
                        StepCrisis(
                            "Autoafirmaciones",
                            "Repetir frases:\n“Soy fuerte y esto pasará”\n“Tengo el control de mi mente y mi cuerpo”\n“Me merezco tener alegría y plenitud”",
                            "image_step_3"
                        )
                    )
                }
                _loading.value = false
            } catch (e: Exception) {
                _loading.value = false
                Log.d("CrisisHandlingViewModel", "Exception in fetchCrisisSteps $e")
            }
        }
    }

    fun nextStep() {
        if (currentStepIndex < steps.size - 1) {
            currentStepIndex++
        } else {
            stopMusic()
            shouldShowModal = true
        }
    }

    fun showModal() {
        shouldShowModal = true
    }

    fun dismissModal() {
        shouldShowModal = false
    }

    fun showExitModal() {
        shouldShowExitModal = true
    }

    fun dismissExitModal() {
        shouldShowExitModal = false
    }
    fun playMusic(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val storage = FirebaseStorage.getInstance()
            val audioRef = storage.reference.child("Songs/song.mp3")
            val url = audioRef.downloadUrl.await().toString()
            player = MediaPlayer().apply {
                setDataSource(url)
                prepare()
                setVolume(0.45f, 0.45f)
                start()

                setOnCompletionListener {
                    start()

                }
            }


            }

                }

    fun pauseMusic() {
        player?.pause()
        isPlaying = false
    }

    fun stopMusic() {
        player?.release()
        player = null
        isPlaying = false
    }

    override fun onCleared() {
        super.onCleared()
        stopMusic()
    }
}

