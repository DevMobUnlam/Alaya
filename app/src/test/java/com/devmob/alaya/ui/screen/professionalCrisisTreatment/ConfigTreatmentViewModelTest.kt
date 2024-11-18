package com.devmob.alaya.ui.screen.professionalCrisisTreatment

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.devmob.alaya.data.FirebaseClient
import com.devmob.alaya.domain.SaveCrisisTreatmentUseCase
import com.onesignal.OneSignal
import com.devmob.alaya.domain.model.FirebaseResult
import com.devmob.alaya.domain.model.OptionTreatment
import io.mockk.MockKAnnotations
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ConfigTreatmentViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: ConfigTreatmentViewModel

    @MockK
    private lateinit var saveCrisisUseCase: SaveCrisisTreatmentUseCase

    @MockK
    private lateinit var customActivity: OptionTreatment

    @MockK
    private lateinit var listOfTreatment: List<OptionTreatment>
    private lateinit var firebaseClient: FirebaseClient

    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var viewModel: ConfigTreatmentViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
        Dispatchers.setMain(testDispatcher)
        coEvery { saveCrisisUseCase("email", listOfTreatment) } returns FirebaseResult.Success

        mockkObject(OneSignal)
        justRun { OneSignal.login(any()) }
        every { OneSignal.User.addAlias("ALIAS_FIREBASE_ID","") } returns mockk()
        viewModel = ConfigTreatmentViewModel(saveCrisisUseCase)
    }

    @Test
    fun `given a OptionTreatment, when addCustomActivity, then add the Option to the treatmentOptions`() {
        // GIVEN
        val actualListSize = viewModel.treatmentOptions.size
        val expectedListSize = actualListSize + 1
        customActivity

        //WHEN
        viewModel.addCustomActivity(customActivity)
        val newListSize = viewModel.treatmentOptions.size

        //THEN
        assertEquals(expectedListSize,newListSize)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearMocks(saveCrisisUseCase, firebaseClient)
    }

    @Test
    fun `given an email and a treatmentList, when saveCrisisTreatment, then navigate`() {
        //GIVEN
        val email = "email"
        listOfTreatment
        val navigateExpected = true

        //WHEN
        viewModel.saveCrisisTreatment(email, listOfTreatment)
        val result = viewModel.navigate.value

        //THEN
        assertEquals(navigateExpected, result)
    }

    @Test
    fun `given an incorrect email and a treatmentList, when saveCrisisTreatment, then showError`() {
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        //GIVEN
        val email = "incorrectEmail"
        listOfTreatment
        val errorExpected = true

        //WHEN
        viewModel.saveCrisisTreatment(email, listOfTreatment)
        val result = viewModel.showError.value

        //THEN
        assertEquals(errorExpected, result)
    }
}