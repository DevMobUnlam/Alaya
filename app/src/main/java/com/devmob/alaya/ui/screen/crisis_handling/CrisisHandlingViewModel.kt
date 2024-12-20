package com.devmob.alaya.ui.screen.crisis_handling

import android.util.Log
import androidx.compose.runtime.MutableState
import android.media.MediaPlayer
import android.speech.tts.TextToSpeech
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devmob.alaya.data.FirebaseClient
import com.devmob.alaya.domain.SaveCrisisRegistrationUseCase
import com.devmob.alaya.domain.model.CrisisDetailsDB
import com.devmob.alaya.domain.model.FirebaseResult
import com.devmob.alaya.domain.GetCrisisTreatmentUseCase
import com.devmob.alaya.domain.model.OptionTreatment
import com.devmob.alaya.domain.model.StepCrisis
import kotlinx.coroutines.launch
import java.util.Date
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await

class CrisisHandlingViewModel(
    private val saveCrisisRegistrationUseCase: SaveCrisisRegistrationUseCase,
    private val getCrisisTreatmentUseCase: GetCrisisTreatmentUseCase
) : ViewModel() {
    var steps by mutableStateOf<List<StepCrisis>>(emptyList())
    var optionTreatmentsList by mutableStateOf<List<OptionTreatment>?>(null)
    var currentStepIndex by mutableIntStateOf(0)
    var shouldShowModal by mutableStateOf(false)
    var shouldShowExitModal by mutableStateOf(false)
    var isPlaying by mutableStateOf(false)
    private var shouldVoiceSpeak = true
    var isVoiceOn by mutableStateOf(false)
    private var player: MediaPlayer? = null
    val currentUser = FirebaseClient().auth.currentUser

    private var startTime: Date? = null
    private var endTime: Date? = null

    val toolsUsed = mutableListOf<String>()

    private val _loading = mutableStateOf(true)
    val loading: MutableState<Boolean>
        get() = _loading

    val currentStep: StepCrisis?
        get() = if (steps.isNotEmpty()) {
            steps[currentStepIndex]
        } else null

    init {
        fetchCrisisSteps()
        startCrisisHandling()
    }

    private fun startCrisisHandling() {
        startTime = Date()
    }

    fun endCrisisHandling() {
        endTime = Date()
        saveCrisisData()
    }

    private fun fetchCrisisSteps() {
        var stepCrisisList: List<StepCrisis>
        viewModelScope.launch {
            _loading.value = true
            try {
                // Obtén los tratamientos del terapeuta
                optionTreatmentsList = getCrisisTreatmentUseCase()

                // Si hay tratamientos por terapeuta
                if (!optionTreatmentsList.isNullOrEmpty()) {
                    stepCrisisList = optionTreatmentsList?.map { option ->
                        StepCrisis(
                            title = option.title,
                            description = option.description,
                            image = option.imageUri
                        )
                    } ?: getDefaultStepCrises()
                    steps = stepCrisisList
                } else {
                    // Si no hay tratamiento por terapeuta, carga los pasos predeterminados
                    steps = getDefaultStepCrises()
                }
                _loading.value = false
            } catch (e: Exception) {
                steps = getDefaultStepCrises()
                _loading.value = false
                Log.d("CrisisHandlingViewModel", "Exception in fetchCrisisSteps $e")
            }
        }
    }

    private fun getDefaultStepCrises() = listOf(
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

    fun nextStep() {
        currentStep?.let { addTool(it.title) }
        if (currentStepIndex < steps.size - 1) {
            currentStepIndex++
        } else {
            stopMusic()
            shouldShowModal = true
            endCrisisHandling()
        }
    }

    private fun saveCrisisData() {
        viewModelScope.launch {
            val crisisDetails = CrisisDetailsDB(
                start = startTime,
                end = endTime,
                place = null,
                bodySensations = emptyList(),
                tools = toolsUsed,
                emotions = emptyList(),
                notes = null,
                completed = false
            )

            val result = saveCrisisRegistrationUseCase(crisisDetails)
            if (result is FirebaseResult.Success) {
                println("Registro de crisis guardado exitosamente")
            } else {
                println("Error al guardar el registro de crisis")
            }
        }
    }

    private fun addTool(tool: String) {
        if (!toolsUsed.contains(tool)) {
            toolsUsed.add(tool)
        }
    }

    fun showModal() {
        shouldShowModal = true
        currentStep?.let { addTool(it.title) }
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

    fun playMusic() {
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

    fun startTextToSpeech(textToSpeech: TextToSpeech, isTtsInitialized: Boolean) {
        stopTextToSpeech(textToSpeech)
        if (shouldVoiceSpeak && isVoiceOn) {
            if (isTtsInitialized) {
                if (currentStep != null) {
                    textToSpeech.speak(
                        currentStep?.title,
                        TextToSpeech.QUEUE_ADD,
                        null,
                        null
                    )
                }
                if (currentStep != null) {
                    textToSpeech.speak(
                        currentStep?.description,
                        TextToSpeech.QUEUE_ADD,
                        null,
                        null
                    )
                }

            }
        }

    }

    fun stopTextToSpeech(textToSpeech: TextToSpeech) {
        if (textToSpeech.isSpeaking) {
            textToSpeech.stop()
        }
    }

    fun setShouldSpeakVoice(status: Boolean) {
        shouldVoiceSpeak = status
    }

    fun onMuteVoice(textToSpeech: TextToSpeech, isTtsInitialized: Boolean) {
        if (isVoiceOn) {
            stopTextToSpeech(textToSpeech)
            isVoiceOn = false
        } else {
            isVoiceOn = true

            if (isTtsInitialized) {
                stopTextToSpeech(textToSpeech)
                startTextToSpeech(textToSpeech, true)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        stopMusic()
    }
}