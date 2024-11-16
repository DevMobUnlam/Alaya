package com.devmob.alaya.ui.screen

import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.devmob.alaya.ui.screen.ContainmentNetwork.Contact.ContactViewModel
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*

class ContactViewModelTest {

//    @get:Rule
//    val instantTaskExecutorRule = InstantTaskExecutorRule()
//
//    private lateinit var viewModel: ContactViewModel
//    private lateinit var observer: Observer<String?>
//    private lateinit var context: Context
//
//    @Before
//    fun setUp() {
//        viewModel = ContactViewModel()
//        observer = mock(Observer::class.java) as Observer<String?>
//        context = mock(Context::class.java)
//
//        viewModel.errorMessage.observeForever(observer)
//    }

//    @Test
//    fun `formatPhoneNumberForWhatsApp formats number correctly for WhatsApp`() {
//        assertEquals("+54 9 1123456789", viewModel.formatPhoneNumberForWhatsApp("1123456789"))
//    }

//ESTOS TYEST FALLAN, BUSCAR LA FORMA DE TESTEAR LOS INTENT
 /*   @Test
    fun `sendWhatsAppMessage sends intent when WhatsApp is installed`() {
        val phoneNumber = "+5491123456789"
        val message = "Hola"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/$phoneNumber?text=${Uri.encode(message)}"))
        intent.setPackage("com.whatsapp")

        val resolveInfo = mock(ResolveInfo::class.java)
        `when`(context.packageManager.resolveActivity(intent, 0)).thenReturn(resolveInfo)

        viewModel.sendWhatsAppMessage(context, phoneNumber, message)

        verify(context).startActivity(intent)
    }

    @Test
    fun `sendWhatsAppMessage sets error message when WhatsApp is not installed`() {
        val phoneNumber = "+5491123456789"
        val message = "Hola"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/$phoneNumber?text=${Uri.encode(message)}"))
        intent.setPackage("com.whatsapp")

        `when`(context.packageManager.resolveActivity(intent, 0)).thenReturn(null)

        viewModel.sendWhatsAppMessage(context, phoneNumber, message)

        assertEquals("WhatsApp no está instalado", viewModel.errorMessage.value)
        verify(observer).onChanged("WhatsApp no está instalado")
    }

    @Test
    fun `makeCall starts dial intent`() {
        val phoneNumber = "+5491123456789"
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phoneNumber")
        }

        viewModel.makeCall(context, phoneNumber)

        verify(context).startActivity(intent)
    }*/

//    @Test
//    fun `error message is null initially`() {
//        assertNull(viewModel.errorMessage.value)
//    }

}