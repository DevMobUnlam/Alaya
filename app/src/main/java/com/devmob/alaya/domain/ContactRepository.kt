package com.devmob.alaya.domain

import android.net.Uri
import com.devmob.alaya.domain.model.Contact
import com.devmob.alaya.domain.model.FirebaseResult

interface ContactRepository {
    suspend fun updateContacts(email: String, contacts: List<Contact>): FirebaseResult
    fun listenToContacts(email: String, onContactsUpdated: (List<Contact>) -> Unit)
    suspend fun uploadImageToStorage(uri: Uri, contactId: String): String?
    fun getDefaultContact(): Contact
}