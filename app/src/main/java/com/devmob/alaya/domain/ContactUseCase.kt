package com.devmob.alaya.domain

import android.net.Uri
import com.devmob.alaya.data.ContactRepositoryImpl
import com.devmob.alaya.data.GetUserRepositoryImpl
import com.devmob.alaya.data.preferences.SharedPreferences
import com.devmob.alaya.domain.model.Contact
import com.devmob.alaya.domain.model.FirebaseResult

class ContactUseCase(private val prefs: SharedPreferences) {
    private val contactRepositoryImpl = ContactRepositoryImpl()
    private val userRepository = GetUserRepositoryImpl()

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

    suspend fun getContacts(email: String): List<Contact> {
        return contactRepositoryImpl.getContacts(email)
    }

    suspend fun deleteContact(email: String, contact: Contact): FirebaseResult {
        return contactRepositoryImpl.deleteContact(email, contact)
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
                val photoUrl = contactRepositoryImpl.uploadImageToStorage(photoUri, contact.contactId)
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