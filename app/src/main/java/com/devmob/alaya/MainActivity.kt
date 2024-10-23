package com.devmob.alaya


import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.mutableStateOf
import java.util.Locale
import androidx.navigation.compose.rememberNavController
import com.devmob.alaya.ui.theme.AlayaTheme

class MainActivity : ComponentActivity(), TextToSpeech.OnInitListener {

    private lateinit var textToSpeech: TextToSpeech
    private var isTtsInitialized = mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        textToSpeech = TextToSpeech(this,this)

        enableEdgeToEdge()
        setContent {
            AlayaTheme {
                val navController = rememberNavController()
                MainContent(navController, textToSpeech, isTtsInitialized.value)
            }
        }
    }

    override fun onInit(status: Int) {
        if(status == TextToSpeech.SUCCESS){
            textToSpeech.language = Locale("es")
            textToSpeech.setPitch(0.85f)
            textToSpeech.setSpeechRate(1.08F)
            isTtsInitialized.value = true
        } else{
            isTtsInitialized.value = false
        }
    }

    override fun onDestroy(){
        if(::textToSpeech.isInitialized){
            textToSpeech.stop()
            textToSpeech.shutdown()
        }
        super.onDestroy()
    }
}
