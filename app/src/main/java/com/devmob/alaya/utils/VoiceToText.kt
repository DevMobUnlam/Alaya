package com.devmob.alaya.utils

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class VoiceToText(private val context: Context) : RecognitionListener {


    private val _state = MutableStateFlow(VoiceToTextParserState())
    val state = _state.asStateFlow()


    private val recognizer = SpeechRecognizer.createSpeechRecognizer(context)
    private var isListening = false


    fun startListening(languageCode: String = "es-ES") {
        _state.update { VoiceToTextParserState() }
        if (!SpeechRecognizer.isRecognitionAvailable(context)) {
            _state.update {
                it.copy(
                    error = "Recognition is not available"
                )
            }
        }


        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, languageCode)
            putExtra(
                RecognizerIntent.EXTRA_PARTIAL_RESULTS,
                true
            )
            putExtra(
                RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS,
                5000L
            )
            putExtra(
                RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS,
                3000L
            )
        }


        recognizer.setRecognitionListener(this)
        recognizer.startListening(intent)


        _state.update {
            it.copy(
                isSpeaking = true
            )
        }
        isListening = true
    }


    fun stopListening() {
        _state.update {
            it.copy(
                isSpeaking = false
            )
        }
        recognizer.stopListening()
        isListening = false
    }


    override fun onReadyForSpeech(params: Bundle?) {
        _state.update {
            it.copy(
                error = null
            )
        }
    }


    override fun onBeginningOfSpeech() = Unit


    override fun onRmsChanged(rmsdB: Float) = Unit


    override fun onBufferReceived(buffer: ByteArray?) = Unit


    override fun onEndOfSpeech() {
        _state.update {
            it.copy(
                isSpeaking = false
            )
        }
    }


    override fun onError(error: Int) {
        if (error == SpeechRecognizer.ERROR_CLIENT) {
            return
        }
        _state.update {
            it.copy(
                error = "Error: $error"
            )
        }
        isListening = false
    }


    override fun onResults(results: Bundle?) {
        results
            ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            ?.getOrNull(0)
            ?.let { result ->
                _state.update {
                    it.copy(
                        spokenText = result
                    )
                }
            }
    }


    override fun onPartialResults(partialResults: Bundle?) {
        partialResults
            ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            ?.getOrNull(0)
            ?.let { partialText ->
                Log.i("VoiceToText: Voice", partialText)
                _state.update {
                    it.copy(
                        spokenText = partialText
                    )
                }
            }
    }


    override fun onEvent(eventType: Int, params: Bundle?) = Unit
}


data class VoiceToTextParserState(
    val spokenText: String = "",
    val isSpeaking: Boolean = false,
    val error: String? = null
)
