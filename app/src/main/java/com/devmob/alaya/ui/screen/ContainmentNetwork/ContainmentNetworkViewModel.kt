package com.devmob.alaya.ui.screen.ContainmentNetwork

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devmob.alaya.domain.model.Contact

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
            Contact("1","Patricia Psicóloga", "123456789", "https://img.freepik.com/foto-gratis/vista-frontal-mujer-probando-colores_23-2150538715.jpg?ga=GA1.2.509011693.1726456853&semt=ais_hybrid"),
            Contact("2","Juan Gómez", "987654321", "https://img.freepik.com/foto-gratis/vista-frontal-mujer-probando-colores_23-2150538715.jpg?ga=GA1.2.509011693.1726456853&semt=ais_hybrid"),
            Contact("3","Marta López", "456123789", "https://img.freepik.com/foto-gratis/vista-frontal-mujer-probando-colores_23-2150538715.jpg?ga=GA1.2.509011693.1726456853&semt=ais_hybrid"),
            Contact("4","Pedro Díaz", "789123456", "https://img.freepik.com/foto-gratis/vista-frontal-mujer-probando-colores_23-2150538715.jpg?ga=GA1.2.509011693.1726456853&semt=ais_hybrid")
        )
    }
}