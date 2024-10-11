package com.devmob.alaya.ui.screen

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.devmob.alaya.domain.model.Contact
import com.devmob.alaya.ui.screen.ContainmentNetwork.ContainmentNetworkViewModel
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertFalse
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock

class ContainmentNetworkViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: ContainmentNetworkViewModel
    private lateinit var observer: Observer<List<Contact>>

    @Before
    fun setUp() {
        viewModel = ContainmentNetworkViewModel()
        observer = mock(Observer::class.java) as Observer<List<Contact>>
    }

    @Test
    fun `initial contacts are loaded`() {
        val expectedContacts = viewModel.contacts.value
        assertEquals(4, expectedContacts?.size)
        assertEquals("Patricia Psicóloga", expectedContacts?.get(0)?.name)
    }

    @Test
    fun `addContact adds a new contact`() {
        val newContact = Contact("5", "Nuevo Contacto", "555-5555", "")
        viewModel.addContact(newContact)

        val contacts = viewModel.contacts.value
        assertEquals(5, contacts?.size)
        assertEquals("Nuevo Contacto", contacts?.last()?.name)
    }

    @Test
    fun `deleteContact removes the specified contact`() {
        val contactToDelete = viewModel.contacts.value?.get(0)
        contactToDelete?.let {
            viewModel.deleteContact(it)
        }

        val contacts = viewModel.contacts.value
        assertEquals(3, contacts?.size)
        assertFalse(contacts?.any { it.contactId == contactToDelete?.contactId } ?: true)
    }

    @Test
    fun `getContactById returns the correct contact`() {
        val contactId = "2" // Brenda
        val contact = viewModel.getContactById(contactId)

        assertEquals("Brenda", contact?.name)
    }

    @Test
    fun `getContactById returns null for non-existent contact`() {
        val contactId = "non-existent-id"
        val contact = viewModel.getContactById(contactId)

        assertNull(contact)
    }

    @Test
    fun `editContact updates the specified contact`() {
        val updatedContact = Contact("1", "Patricia Psicóloga Actualizada", "123456789", "")
        viewModel.editContact(updatedContact)

        val contact = viewModel.getContactById("1")
        assertEquals("Patricia Psicóloga Actualizada", contact?.name)
    }
}