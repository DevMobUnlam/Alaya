package com.devmob.alaya.domain

import android.net.Uri
import android.util.Log
import com.devmob.alaya.data.ContactRepositoryImpl
import com.devmob.alaya.data.GetUserRepositoryImpl
import com.devmob.alaya.data.preferences.SharedPreferences
import com.devmob.alaya.domain.model.Contact
import com.devmob.alaya.domain.model.FirebaseResult

class ContactUseCase(
    private val prefs: SharedPreferences,
    private val contactRepositoryImpl: ContactRepositoryImpl,
    private val userRepository: GetUserRepositoryImpl
) {
    suspend fun addContact(contact: Contact): FirebaseResult {
        val email = prefs.getEmail()
        val user = email?.let { userRepository.getUser(it) }
        val updatedContacts = user?.containmentNetwork?.toMutableList() ?: mutableListOf()
        if (!updatedContacts.any { it.contactId == contact.contactId }) {
            updatedContacts.add(contact)
        }
        return if (!email.isNullOrEmpty()) {
            contactRepositoryImpl.updateContacts(email, updatedContacts)
        } else {
            FirebaseResult.Error(Throwable("Email is null"))
        }
    }

    suspend fun getContacts(): List<Contact> {
        return try {
            val email = prefs.getEmail()
            val user = email?.let { userRepository.getUser(it) }
            val contacts = user?.containmentNetwork ?: emptyList()
            val defaultContact = contactRepositoryImpl.getDefaultContact()
            listOf(defaultContact) + contacts.filter { it.contactId != defaultContact.contactId }
        } catch (e: Exception) {
            Log.e("Firebase", "Error al obtener los contactos", e)
            emptyList()
        }
    }

    suspend fun deleteContact(contact: Contact): FirebaseResult {
        return try {
            val email = prefs.getEmail()
            val user = email?.let { userRepository.getUser(it) }

            val updatedContacts = user?.containmentNetwork?.toMutableList() ?: mutableListOf()
            updatedContacts.removeIf { it.contactId == contact.contactId }
            return if (!email.isNullOrEmpty()) {
                contactRepositoryImpl.updateContacts(email, updatedContacts)
            } else {
                FirebaseResult.Error(Throwable("Email is null"))
            }
        } catch (e: Exception) {
            FirebaseResult.Error(e)
        }
    }

    suspend fun editContact(contact: Contact): FirebaseResult {
        val email = prefs.getEmail()
        val user = email?.let { userRepository.getUser(it) }
        val updatedContacts = user?.containmentNetwork?.toMutableList() ?: mutableListOf()
        val index = updatedContacts.indexOfFirst { it.contactId == contact.contactId }

        if (index != -1) {
            val currentContact = updatedContacts[index]
            var updatedContact = contact
            if (!contact.photo.isNullOrEmpty() && contact.photo != currentContact.photo) {
                val photoUri = Uri.parse(contact.photo)
                val photoUrl =
                    contactRepositoryImpl.uploadImageToStorage(photoUri, contact.contactId)
                if (photoUrl != null) {
                    updatedContact = contact.copy(photo = photoUrl)
                } else {
                    return FirebaseResult.Error(Exception("Error al subir la foto"))
                }
            }
            updatedContacts[index] = updatedContact
        }
        return if (!email.isNullOrEmpty()) {
            contactRepositoryImpl.updateContacts(email, updatedContacts)
        } else {
            FirebaseResult.Error(Throwable("Email is null"))
        }
    }

    fun listenToContacts(email: String, onContactsUpdated: (List<Contact>) -> Unit) {
        contactRepositoryImpl.listenToContacts(email, onContactsUpdated)
    }
}