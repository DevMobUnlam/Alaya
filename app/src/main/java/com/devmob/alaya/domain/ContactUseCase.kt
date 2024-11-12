package com.devmob.alaya.domain

import android.net.Uri
import com.devmob.alaya.data.ContactRepositoryImpl
import com.devmob.alaya.domain.model.Contact
import com.devmob.alaya.domain.model.FirebaseResult

class ContactUseCase() {
    private val contactRepositoryImpl = ContactRepositoryImpl()

    suspend fun addContact(email: String, contact: Contact): FirebaseResult {
        return contactRepositoryImpl.addContact(email, contact)
    }

    suspend fun getContacts(email: String): List<Contact> {
        return contactRepositoryImpl.getContacts(email)
    }

    suspend fun deleteContact(email: String, contact: Contact): FirebaseResult {
        return contactRepositoryImpl.deleteContact(email, contact)
    }

    suspend fun editContact(email: String, contact: Contact): FirebaseResult {
        return contactRepositoryImpl.editContact(email, contact)
    }

    fun listenToContacts(email: String, onContactsUpdated: (List<Contact>) -> Unit) {
        contactRepositoryImpl.listenToContacts(email, onContactsUpdated)
    }

    suspend fun uploadImageToStorage(uri : Uri, contactId: String) {
        contactRepositoryImpl.uploadImageToStorage(uri, contactId)
    }
}