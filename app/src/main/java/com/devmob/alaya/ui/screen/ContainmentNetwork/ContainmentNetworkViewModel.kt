package com.devmob.alaya.ui.screen.ContainmentNetwork

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.ContactsContract
import androidx.lifecycle.*
import com.devmob.alaya.data.FirebaseClient
import com.devmob.alaya.domain.ContactUseCase
import com.devmob.alaya.domain.model.Contact
import com.devmob.alaya.domain.model.FirebaseResult
import kotlinx.coroutines.launch

class ContainmentNetworkViewModel(
     val contactUseCase: ContactUseCase
) : ViewModel() {

    private val _contacts = MutableLiveData<List<Contact>>(emptyList())
    val contacts: LiveData<List<Contact>> get() = _contacts

    init {
        listenToContacts()
    }

    fun listenToContacts() {
        val email = FirebaseClient().auth.currentUser?.email
        if (email != null) {
            contactUseCase.listenToContacts(email) { updatedContacts ->
                _contacts.postValue(updatedContacts)
            }
        }
    }


    private val _operationStatus = MutableLiveData<String>()

    fun addContactFromPhone(context: Context, contactUri: Uri) {
        val contentResolver = context.contentResolver
        val contact = loadContactFromUri(contentResolver, contactUri)

        if (contact != null) {
            viewModelScope.launch {
                val result = contactUseCase.addContact(contact)
                handleFirebaseResult(result, "Contacto agregado con éxito", "Error al agregar contacto")
            }
        } else {
            _operationStatus.value = "No se pudo cargar el contacto"
        }
    }

    fun editContact(email: String, contact: Contact) {
        viewModelScope.launch {
            val result = contactUseCase.editContact(email, contact)
            handleFirebaseResult(result, "Contacto editado con éxito", "Error al editar contacto")

            if (result is FirebaseResult.Success) {
                _contacts.value = _contacts.value?.map {
                    if (it.contactId == contact.contactId) contact else it
                }
            }
        }
    }

    fun deleteContact(email: String, contact: Contact) {
        viewModelScope.launch {
            val result = contactUseCase.deleteContact(email, contact)
            handleFirebaseResult(result, "Contacto eliminado con éxito", "Error al eliminar contacto")

            if (result is FirebaseResult.Success) {
                _contacts.value = _contacts.value?.filter { it.contactId != contact.contactId }
            }
        }
    }

    private fun handleFirebaseResult(result: FirebaseResult, successMsg: String, errorMsg: String) {
        _operationStatus.value = when (result) {
            is FirebaseResult.Success -> successMsg
            is FirebaseResult.Error -> "$errorMsg: ${result.t?.message}"
        }
    }

    private fun loadContactFromUri(contentResolver: ContentResolver, contactUri: Uri): Contact? {
        var contact: Contact? = null
        val cursor = contentResolver.query(
            contactUri,
            arrayOf(ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME),
            null, null, null
        )

        cursor?.use {
            if (it.moveToFirst()) {
                val contactId = it.getString(it.getColumnIndexOrThrow(ContactsContract.Contacts._ID))
                val name = it.getString(it.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME))
                val phoneNumber = getPhoneNumber(contentResolver, contactId)
                val photoUri = getPhotoUri(contentResolver, contactId)

                contact = Contact(
                    contactId = contactId,
                    name = name,
                    numberPhone = phoneNumber ?: "",
                    photo = photoUri?.toString() ?: ""
                )
            }
        }
        return contact
    }

    private fun getPhoneNumber(contentResolver: ContentResolver, contactId: String): String? {
        var phoneNumber: String? = null
        val phoneCursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER),
            "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?",
            arrayOf(contactId),
            null
        )

        phoneCursor?.use {
            if (it.moveToFirst()) {
                phoneNumber = it.getString(it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))
            }
        }
        return phoneNumber
    }

    private fun getPhotoUri(contentResolver: ContentResolver, contactId: String): Uri? {
        val photoCursor = contentResolver.query(
            ContactsContract.Data.CONTENT_URI,
            arrayOf(ContactsContract.CommonDataKinds.Photo.PHOTO_URI),
            "${ContactsContract.Data.CONTACT_ID} = ? AND ${ContactsContract.Data.MIMETYPE} = ?",
            arrayOf(contactId, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE),
            null
        )

        photoCursor?.use {
            if (it.moveToFirst()) {
                val photoUriString = it.getString(it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Photo.PHOTO_URI))
                return if (photoUriString != null) Uri.parse(photoUriString) else null
            }
        }
        return null
    }
}
