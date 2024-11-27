package com.devmob.alaya.domain

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.devmob.alaya.data.ContactRepositoryImpl
import com.devmob.alaya.data.GetUserRepositoryImpl
import com.devmob.alaya.data.preferences.SharedPreferences
import com.devmob.alaya.domain.model.Contact
import com.devmob.alaya.domain.model.FirebaseResult
import com.devmob.alaya.domain.model.User
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
@OptIn(ExperimentalCoroutinesApi::class)
class ContactUseCaseTest {

    private lateinit var contactUseCase: ContactUseCase

    @MockK
    private lateinit var prefs: SharedPreferences

    @MockK
    private lateinit var contactRepository: ContactRepositoryImpl

    @MockK
    private lateinit var userRepository: GetUserRepositoryImpl

    @MockK
    private lateinit var userMock: User

    @MockK
    private lateinit var exception: Exception

    @MockK
    private lateinit var contact: Contact

    @MockK
    private lateinit var contact1: Contact

    @MockK
    private lateinit var contact2: Contact

    @Mock
    private var list: List<Contact> = mutableListOf()

    private val testDispatcher = UnconfinedTestDispatcher()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
        Dispatchers.setMain(testDispatcher)
        coEvery { userRepository.getUser("email") } returns userMock
        coEvery { contactRepository.updateContacts("email", any()) } returns FirebaseResult.Success

        contactUseCase = ContactUseCase(prefs, contactRepository, userRepository)
    }

    @Test
    fun `given a valid contact, when addContact is called, then return Success`(): Unit =
        runBlocking {
            //GIVEH
            every { prefs.getEmail() } returns "email"

            //WHEN
            val result = contactUseCase.addContact(contact)
            val expected = FirebaseResult.Success

            //THEN
            assertEquals(expected, result)
        }

    @Test
    fun `given a null email, when addContact is called, then return Error`(): Unit =
        runBlocking {
            //GIVEH
            every { prefs.getEmail() } returns null
            //WHEN
            val result = contactUseCase.addContact(contact)

            //THEN
            assert(result is FirebaseResult.Error)
        }

    @Test
    fun `given a currentUser email, when getContacts is called, then return a List`(): Unit =
        runBlocking {
            //GIVEH/
            every { prefs.getEmail() } returns "email"
            every { contactRepository.getDefaultContact() } returns contact

            //WHEN
            val contacts = contactUseCase.getContacts()

            //THEN
            assertEquals(1, contacts.size)
            assertEquals(contact, contacts.first())
        }

    @Test
    fun `given a current user email, when getContacts is called and a throws a exception, then the list is empty`(): Unit =
        runBlocking {
            //GIVEH/
            mockkStatic(Log::class)
            every { Log.e(any(), any()) } returns 0

            every { prefs.getEmail() } returns "email"
            coEvery { userRepository.getUser("email") } throws Exception("Error al obtener los contactos")

            //WHEN
            val result = contactUseCase.getContacts()

            //THEN
            assertTrue(result.isEmpty())
        }

    @Test
    fun `given a contact, when deleteContact is called, then delete the contact and return Success`() =
        runBlocking {
            //GIVEN
            every { prefs.getEmail() } returns "email"
            every { userMock.containmentNetwork } returns listOf(contact1, contact2)

            //WHEN
            contactUseCase.deleteContact(contact)
            val result = userMock.containmentNetwork?.size

            //THEN
            assertEquals(2, result)
        }

    @Test
    fun `given a contact, when deleteContact is called and fails, then return Error`() =
        runBlocking {
            //GIVEN
            every { prefs.getEmail() } returns "email"
            coEvery {
                contactRepository.updateContacts(
                    "email",
                    list
                )
            } returns FirebaseResult.Error(exception)

            //WHEN
            val result = contactUseCase.deleteContact(contact)

            //THEN
            assert(result is FirebaseResult.Error)
        }

    @Test
    fun `given a contact, when updateContact is called, then edit the contact and return Success`() =
        runBlocking {
            //GIVEN
            every { prefs.getEmail() } returns "email"
            coEvery {
                contactRepository.uploadImageToStorage(
                    any(),
                    any()
                )
            } returns "uploaded_photo_url"
            coEvery {
                contactRepository.updateContacts(
                    any(),
                    any()
                )
            } returns FirebaseResult.Success

            //WHEN
            val result = contactUseCase.editContact(contact)
            val expected = FirebaseResult.Success

            //THEN
            assertEquals(expected, result)
            coVerify { contactRepository.updateContacts("email", any()) }
        }

    @Test
    fun `given a contact and null email, when updateContact is called, then return Error`() =
        runBlocking {
            //GIVEH
            every { prefs.getEmail() } returns null

            //WHEN
            val result = contactUseCase.editContact(contact)

            //THEN
            assertTrue(result is FirebaseResult.Error)
        }
}