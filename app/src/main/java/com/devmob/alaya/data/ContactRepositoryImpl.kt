package com.devmob.alaya.data

import android.net.Uri
import android.util.Log
import com.devmob.alaya.domain.ContactRepository
import com.devmob.alaya.domain.model.Contact
import com.devmob.alaya.domain.model.FirebaseResult
import com.devmob.alaya.domain.model.User
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class ContactRepositoryImpl : ContactRepository {
    private val db = FirebaseClient().db

    override suspend fun updateContacts(email: String, contacts: List<Contact>): FirebaseResult {
        return try {
            val userRef = db.collection("users").document(email)
            userRef.update("containmentNetwork", contacts).await()
            FirebaseResult.Success
        } catch (e: Exception) {
            FirebaseResult.Error(e)
        }
    }

    override fun listenToContacts(email: String, onContactsUpdated: (List<Contact>) -> Unit) {
        val userRef = db.collection("users").document(email)

        userRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w("Firebase", "Error al escuchar los contactos: ", e)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                val user = snapshot.toObject(User::class.java)
                val contacts = user?.containmentNetwork ?: emptyList()

                val defaultContact = getDefaultContact()

                val updatedContacts = mutableListOf(defaultContact)
                updatedContacts.addAll(contacts.filter { it.contactId != defaultContact.contactId }) // Evita duplicados

                onContactsUpdated(updatedContacts)
            } else {
                Log.w("Firebase", "Documento del usuario no encontrado")
                onContactsUpdated(emptyList())
            }
        }
    }


    override suspend fun getContacts(email: String): List<Contact> {
        return try {
            val snapshot = db.collection("users").document(email).get().await()
            val user = snapshot.toObject(User::class.java)

            val contacts = user?.containmentNetwork ?: emptyList()
            val defaultContact = getDefaultContact()

            listOf(defaultContact) + contacts.filter { it.contactId != defaultContact.contactId }
        } catch (e: Exception) {
            Log.e("Firebase", "Error al obtener los contactos", e)
            emptyList()
        }
    }

    override suspend fun deleteContact(email: String, contact: Contact): FirebaseResult {
        return try {
            val userRef = db.collection("users").document(email)

            val snapshot = userRef.get().await()
            val user = snapshot.toObject(User::class.java)

            val updatedContacts = user?.containmentNetwork?.toMutableList() ?: mutableListOf()
            updatedContacts.removeIf { it.contactId == contact.contactId }

            userRef.update("containmentNetwork", updatedContacts).await()

            FirebaseResult.Success
        } catch (e: Exception) {
            FirebaseResult.Error(e)
        }
    }

    override suspend fun editContact(email: String, contact: Contact): FirebaseResult {
        return try {
            val userRef = db.collection("users").document(email)

            val snapshot = userRef.get().await()
            val user = snapshot.toObject(User::class.java)

            val updatedContacts = user?.containmentNetwork?.toMutableList() ?: mutableListOf()
            val index = updatedContacts.indexOfFirst { it.contactId == contact.contactId }

            if (index != -1) {
                val currentContact = updatedContacts[index]
                var updatedContact = contact

                if (contact.photo != null && contact.photo.isNotEmpty() && contact.photo != currentContact.photo) {
                    val photoUri = Uri.parse(contact.photo)
                    val photoUrl = uploadImageToStorage(photoUri, contact.contactId)
                    if (photoUrl != null) {
                        updatedContact = contact.copy(photo = photoUrl)
                    } else {
                        return FirebaseResult.Error(Exception("Error al subir la foto"))
                    }
                }

                updatedContacts[index] = updatedContact
            }

            userRef.update("containmentNetwork", updatedContacts).await()

            FirebaseResult.Success
        } catch (e: Exception) {
            FirebaseResult.Error(e)
        }
    }

    override suspend fun uploadImageToStorage(uri: Uri, contactId: String): String? {
        return try {
            val storageRef = FirebaseStorage.getInstance().reference
            val fileRef = storageRef.child("contacts/$contactId.jpg") // o algún identificador único

            fileRef.putFile(uri).await()
            fileRef.downloadUrl.await().toString()
        } catch (e: Exception) {
            null
        }
    }


    private fun getDefaultContact(): Contact {
        return Contact(
            contactId = "4",
            name = "Salud Mental Responde",
            numberPhone = "4863-8888",
            photo = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTGRl55wQMCz4RQVtWifKMYXp00cviVb-BrEw&s"
        )
    }
}
