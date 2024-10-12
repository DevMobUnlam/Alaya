package com.devmob.alaya.ui.screen

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.devmob.alaya.ui.screen.ContainmentNetwork.Contact.ContactViewModel
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*

class ContactViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: ContactViewModel
    private lateinit var observer: Observer<String?>

    @Before
    fun setUp() {
        viewModel = ContactViewModel()
        observer = mock(Observer::class.java) as Observer<String?>
        viewModel.errorMessage.observeForever(observer)
    }

    @Test
    fun `formatPhoneNumberForWhatsApp formats number correctly for WhatsApp`() {
        assertEquals("+54 9 1123456789", viewModel.formatPhoneNumberForWhatsApp("1123456789"))
    }

    @Test
    fun `error message is null initially`() {
        assertNull(viewModel.errorMessage.value)
    }

}