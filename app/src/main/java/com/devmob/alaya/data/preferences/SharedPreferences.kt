package com.devmob.alaya.data.preferences

import android.content.Context
import android.content.SharedPreferences
import com.devmob.alaya.domain.model.UserRole

class SharedPreferences(context: Context) {

    companion object {
        val SHARED_PREFERENCE_NAME = "LOGIN_PREFS"
    }

    private val USER_EMAIL = "USER_EMAIL"
    private val USER_ROLE = "USER_ROL"


    private val storage: SharedPreferences =
        context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)

    fun setUserLoggedIn(email: String, role: UserRole) {
        val editor = storage.edit()
        editor.putString(USER_EMAIL, email).putString(USER_ROLE, role.name).apply()
    }

    fun isLoggedIn(): Boolean {
        return !storage.getString(USER_EMAIL, null).isNullOrEmpty()
    }

    fun getEmail(): String? {
        return storage.getString(USER_EMAIL, null)
    }

    fun getRole(): UserRole {
        val role = storage.getString(USER_ROLE, null)
        return when (role) {
            "PATIENT" -> UserRole.PATIENT
            "PROFESSIONAL" -> UserRole.PROFESSIONAL
            else -> UserRole.NONE
        }
    }

    fun signOut(){
        val editor = storage.edit()
        editor.remove(USER_EMAIL).remove(USER_ROLE).apply()
    }
}
