package com.devmob.alaya.utils

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class VoiceToTextTest {

    private lateinit var voiceToText: VoiceToText

    @MockK
    private lateinit var context: Context

    @MockK
    private lateinit var speechRecognizer: SpeechRecognizer

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
        mockkStatic(SpeechRecognizer::class)
        every { SpeechRecognizer.createSpeechRecognizer(context) } returns speechRecognizer
        voiceToText = VoiceToText(context)
    }

    @Test
    fun `given language code when call startListening then recognizer starts listening`() =
        runBlocking {
            // GIVEN
            val languageCode = "es-ES"

            // WHEN
            voiceToText.startListening(languageCode)

            // THEN
            verify { speechRecognizer.startListening(any()) }
            verify { speechRecognizer.setRecognitionListener(voiceToText) }
            assertEquals(true, voiceToText.state.first().isSpeaking)
        }

    @Test
    fun `when call startListening then create intent with correct parameters`() = runBlocking {
        // GIVEN
        val slot = slot<Intent>()
        every { speechRecognizer.startListening(capture(slot)) } just Runs

        // WHEN
        voiceToText.startListening()

        // THEN
        assertEquals(RecognizerIntent.ACTION_RECOGNIZE_SPEECH, slot.captured.action)
        assertEquals("es-ES", slot.captured.getStringExtra(RecognizerIntent.EXTRA_LANGUAGE))
        assertEquals(
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM,
            slot.captured.getStringExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL)
        )
        assertEquals(
            true,
            slot.captured.getBooleanExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, false)
        )
        assertEquals(
            5000L,
            slot.captured.getLongExtra(
                RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS,
                0
            )
        )
        assertEquals(
            3000L,
            slot.captured.getLongExtra(
                RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS,
                0
            )
        )
    }

    @Test
    fun `when call stopListening then recognizer stops listening`() = runBlocking {
        // WHEN
        voiceToText.stopListening()

        // THEN
        verify { speechRecognizer.stopListening() }
        assertEquals(false, voiceToText.state.first().isSpeaking)
    }

    @Test
    fun `when recognition is not available then update state with error`() = runBlocking {
        // GIVEN
        every { SpeechRecognizer.isRecognitionAvailable(context) } returns false

        // WHEN
        voiceToText.startListening()

        // THEN
        assertEquals("Recognition is not available", voiceToText.state.first().error)
    }

    @Test
    fun `when call onReadyForSpeech then update state with no error`() = runBlocking {
        // WHEN
        voiceToText.onReadyForSpeech(null)

        // THEN
        assertEquals(null, voiceToText.state.first().error)
    }

    @Test
    fun `when call onEndOfSpeech then update state with isSpeaking false`() = runBlocking {
        // WHEN
        voiceToText.onEndOfSpeech()

        // THEN
        assertEquals(false, voiceToText.state.first().isSpeaking)
    }

    @Test
    fun `when call onError then update state with error message`() = runBlocking {
        // WHEN
        voiceToText.onError(1)

        // THEN
        assertEquals("Error: 1", voiceToText.state.first().error)
    }

    @Test
    fun `when call onError with SpeechRecognizer#ERROR_CLIENT then do not update state`() =
        runBlocking {
            // WHEN
            voiceToText.onError(SpeechRecognizer.ERROR_CLIENT)

            // THEN
            assertEquals(null, voiceToText.state.first().error)
        }

    @Test
    fun `when call onResults then update state with results`() = runBlocking {
        // GIVEN
        val results = Bundle().apply {
            putStringArrayList(
                SpeechRecognizer.RESULTS_RECOGNITION,
                arrayListOf("result")
            )
        }

        // WHEN
        voiceToText.onResults(results)

        // THEN
        assertEquals("result", voiceToText.state.first().spokenText)
    }

    @Test
    fun `when call onPartialResults then update state with partial results`() = runBlocking {
        // GIVEN
        val partialResults = Bundle().apply {
            putStringArrayList(
                SpeechRecognizer.RESULTS_RECOGNITION,
                arrayListOf("partialText")
            )
        }

        // WHEN
        voiceToText.onPartialResults(partialResults)

        // THEN
        assertEquals("partialText", voiceToText.state.first().spokenText)
    }
}