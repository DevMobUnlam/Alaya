package com.devmob.alaya.data

import android.net.Uri
import android.util.Log
import com.devmob.alaya.domain.ContactRepository
import com.devmob.alaya.domain.model.Contact
import com.devmob.alaya.domain.model.FirebaseResult
import com.devmob.alaya.domain.model.User
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class ContactRepositoryImpl(
    firebaseClient: FirebaseClient
) : ContactRepository {
    private val db = firebaseClient.db

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


    override fun getDefaultContact(): Contact {
        return Contact(
            contactId = "4",
            name = "Salud Mental Responde",
            numberPhone = "4863-8888",
            photo = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTGRl55wQMCz4RQVtWifKMYXp00cviVb-BrEw&s"
        )
    }
}
