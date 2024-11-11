package com.devmob.alaya.ui.screen.professionalCrisisTreatment

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.devmob.alaya.data.FirebaseClient
import com.devmob.alaya.domain.SaveCrisisTreatmentUseCase
import com.onesignal.OneSignal
import io.mockk.MockKAnnotations
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.mockkObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule

@OptIn(ExperimentalCoroutinesApi::class)
class ConfigTreatmentViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: ConfigTreatmentViewModel

    @MockK
    private lateinit var saveCrisisUseCase: SaveCrisisTreatmentUseCase

    @MockK
    private lateinit var firebaseClient: FirebaseClient

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp(){
        MockKAnnotations.init(this, relaxed = true)
        Dispatchers.setMain(testDispatcher)
        mockkObject(OneSignal)
        justRun { OneSignal.login(any()) }
        every { OneSignal.User.addAlias("ALIAS_FIREBASE_ID","") } returns mockk()
        viewModel = ConfigTreatmentViewModel(saveCrisisUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearMocks(saveCrisisUseCase, firebaseClient)
    }

}