package com.devmob.alaya


import android.os.Bundle
import android.util.Log

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.devmob.alaya.data.FirebaseClient
import com.devmob.alaya.ui.screen.HomeScreen
import com.devmob.alaya.ui.screen.PatientHomeScreenViewmodel
import com.devmob.alaya.ui.theme.AlayaTheme
import com.google.firebase.auth.FirebaseUser

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AlayaTheme {
                Log.d("leandro","Log en el mainactivity")
                val navController = rememberNavController()
                MainContent(navController)
            }
        }
    }
}
