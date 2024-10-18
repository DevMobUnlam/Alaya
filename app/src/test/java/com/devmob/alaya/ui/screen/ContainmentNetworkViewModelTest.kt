package com.devmob.alaya.ui.screen

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.devmob.alaya.domain.model.Contact
import com.devmob.alaya.ui.screen.ContainmentNetwork.ContainmentNetworkViewModel
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.eq
import org.mockito.ArgumentMatchers.isNull
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class ContainmentNetworkViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: ContainmentNetworkViewModel
    private lateinit var observer: Observer<List<Contact>>
    private lateinit var cursor: Cursor
    private lateinit var contactUri: Uri
    private lateinit var contentResolver: ContentResolver
    private lateinit var mockContext: Context

    @Before
    fun setUp() {
        viewModel = ContainmentNetworkViewModel()
        observer = mock(Observer::class.java) as Observer<List<Contact>>
        cursor = mock(Cursor::class.java)
        contactUri = mock(Uri::class.java)
        contentResolver = mock(ContentResolver::class.java)
        mockContext = mock(Context::class.java)
    }

    @Test
    fun `initial contacts are loaded`() {
        val expectedContacts = viewModel.contacts.value
        assertEquals(4, expectedContacts?.size)
        assertEquals("Patricia Psicóloga", expectedContacts?.get(1)?.name)
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

    @Test
    fun `addContactFromPhone adds contact from valid URI`() {
        // Configura el cursor simulado para que tenga datos
        `when`(cursor.moveToFirst()).thenReturn(true)
        `when`(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID)).thenReturn(0)
        `when`(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME)).thenReturn(1)
        `when`(cursor.getString(0)).thenReturn("10")
        `when`(cursor.getString(1)).thenReturn("Juan Pérez")

        // Configura el ContentResolver para devolver el cursor simulado
        `when`(contentResolver.query(eq(contactUri), any(), isNull(), isNull(), isNull()))
            .thenReturn(cursor)

        viewModel.addContactFromPhone(mockContext(contentResolver), contactUri)

        val contacts = viewModel.contacts.value
        assertEquals(5, contacts?.size)
        assertTrue(contacts?.any { it.name == "Juan Pérez" } ?: false)

        verify(cursor).close()
    }

    private fun mockContext(contentResolver: ContentResolver): Context {
        val context = mock(Context::class.java)
        `when`(context.contentResolver).thenReturn(contentResolver)
        return context
    }
}