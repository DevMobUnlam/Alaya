package com.devmob.alaya.domain

import com.devmob.alaya.data.FirebaseClient
import com.devmob.alaya.data.GetUserRepositoryImpl
import com.devmob.alaya.ui.screen.searchUser.SearchUserViewModel
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before

class SaveCrisisRegistrationUseCaseTest{

    private lateinit var saveCrisisRegistrationUseCase: SaveCrisisRegistrationUseCase

    @MockK
    private lateinit var crisisRepository: CrisisRepository

    @MockK
    private lateinit var firebaseClient: FirebaseClient

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        MockKAnnotations.init(this,relaxed = true)
        Dispatchers.setMain(testDispatcher)
        saveCrisisRegistrationUseCase = SaveCrisisRegistrationUseCase(crisisRepository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
        unmockkAll()
    }

    /*

    VERIFICAR QUE SE AGREGUE EL REGISTRO
    VERIFICAR QUE SE OBTENGAN LOS DETALLES DEL ULTIMO REGISTRO DE CRISIS
    VERIFICAR QUE SE ACTUALICEN LOS DATOS DE LA CRISIS Y SE OBTENGA EL RESULTADO

     */
}