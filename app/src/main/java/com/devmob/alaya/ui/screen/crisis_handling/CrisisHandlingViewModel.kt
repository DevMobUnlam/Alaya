package com.devmob.alaya.ui.screen.crisis_handling

import android.content.Context
import android.media.MediaPlayer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewModelScope
import com.devmob.alaya.domain.SaveCrisisRegistrationUseCase
import com.devmob.alaya.domain.model.CrisisDetailsDB
import com.devmob.alaya.domain.model.FirebaseResult
import com.devmob.alaya.domain.model.StepCrisis
import kotlinx.coroutines.launch
import java.util.Date
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class CrisisHandlingViewModel (
    private val saveCrisisRegistrationUseCase: SaveCrisisRegistrationUseCase = SaveCrisisRegistrationUseCase())
    : ViewModel() {

    var steps by mutableStateOf<List<StepCrisis>>(emptyList())

    var currentStepIndex by mutableIntStateOf(0)
    var shouldShowModal by mutableStateOf(false)
    var shouldShowExitModal by mutableStateOf(false)
    var isPlaying by mutableStateOf(false)
    private var player: MediaPlayer? = null

    private var startTime: Date? = null
    private var endTime: Date? = null

    val toolsUsed = mutableListOf<String>()

    val currentStep: StepCrisis
        get() = steps[currentStepIndex]

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
        /* TODO // consultar al repositorio para obtener los pasos de la crisis
        viewModelScope.launch {

        }*/
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
                "Autoafirmaciones", "Repetir frases:\n" +
                        "“Soy fuerte y esto pasará”\n" +
                        "“Tengo el control de mi mente y mi cuerpo” \n" +
                        "“Me merezco tener alegría y plenitud”", "image_step_3"
            )
        )
    }

    fun nextStep() {
        addTool(currentStep.title) // si voy al siguiente paso doy por hecho que se utilizo la herramienta
        if (currentStepIndex < steps.size - 1) {
            currentStepIndex++
        } else {
            stopMusic()
            shouldShowModal = true
            endCrisisHandling()
        }
    }

    fun saveCrisisData() {
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

    fun addTool(tool: String) {
        if (!toolsUsed.contains(tool)) {
            toolsUsed.add(tool)
        }
    }

    fun showModal() {
        shouldShowModal = true
        addTool(currentStep.title) //cuando se muestra el modal puede tomar 2 caminos, ahi tambien doy por hecho que realizo la herramienta del paso actual
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