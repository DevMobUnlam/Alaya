package com.devmob.alaya.ui.screen.ContainmentNetwork

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.ContactsContract
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devmob.alaya.domain.model.Contact
import java.util.UUID

class ContainmentNetworkViewModel : ViewModel() {
    private val _contacts = MutableLiveData<List<Contact>>()
    val contacts: LiveData<List<Contact>> get() = _contacts

    init {
        _contacts.value = getSampleContacts()
    }

    fun deleteContact(contact: Contact) {
        _contacts.value = _contacts.value?.filter { it.contactId != contact.contactId }
    }

    fun getContactById(contactId: String?): Contact? {
        return _contacts.value?.find { it.contactId == contactId }
    }

    fun addContact(newContact: Contact) {
        _contacts.value = _contacts.value?.plus(newContact) ?: listOf(newContact)
    }

    fun addContactFromPhone(context: Context, contactUri: Uri) {
        val contentResolver = context.contentResolver
        val contact = loadContactFromUri(contentResolver, contactUri)
        if (contact != null) {
            addContact(contact)
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
                    contactId = UUID.randomUUID().toString(),
                    name = name,
                    numberPhone = phoneNumber ?: "",
                    photo = photoUri?.toString()
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


    fun editContact(updatedContact: Contact) {
        _contacts.value = _contacts.value?.map { contact ->
            if (contact.contactId == updatedContact.contactId) {
                updatedContact
            } else {
                contact
            }
        }
    }


    private fun getSampleContacts(): List<Contact> {
        return listOf(
            Contact("4","Salud Mental Responde", "4863-8888", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTGRl55wQMCz4RQVtWifKMYXp00cviVb-BrEw&s"),
            Contact("1","Patricia Psicóloga", "1136962672", "https://img.freepik.com/foto-gratis/vista-frontal-mujer-probando-colores_23-2150538715.jpg?ga=GA1.2.509011693.1726456853&semt=ais_hybrid"),
            Contact("2","Brenda", "1123919112", "https://img.freepik.com/foto-gratis/feliz-mujer-joven-al-aire-libre_23-2147892780.jpg?t=st=1728279226~exp=1728282826~hmac=07dea132aee500addebbabb315984d8f0cfa60ebd5d191d5c0ab5aca5ac10a39&w=360"),
            Contact("3","Mamá", "1128451531", "https://img.freepik.com/foto-gratis/vista-frontal-mejores-amigos-abrazandose_23-2148440518.jpg?ga=GA1.2.509011693.1726456853&semt=ais_hybrid"),

        )
    }
}