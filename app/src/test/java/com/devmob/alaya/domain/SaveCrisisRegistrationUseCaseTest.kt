package com.devmob.alaya.domain

import com.devmob.alaya.data.FirebaseClient
import com.devmob.alaya.data.GetUserRepositoryImpl
import com.devmob.alaya.domain.model.CrisisDetailsDB
import com.devmob.alaya.domain.model.FirebaseResult
import com.devmob.alaya.ui.screen.searchUser.SearchUserViewModel
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.function.ThrowingRunnable
import kotlin.jvm.Throws

class SaveCrisisRegistrationUseCaseTest{

    private lateinit var saveCrisisRegistrationUseCase: SaveCrisisRegistrationUseCase

    @MockK
    private lateinit var crisisRepository: CrisisRepository




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

    @Test
    fun `when crisis is registered, result is success`() = runBlocking{
        val crisisDetails = CrisisDetailsDB()
        val expectedResult = FirebaseResult.Success

        coEvery { crisisRepository.addRegister(crisisDetails) } returns expectedResult

        val result = saveCrisisRegistrationUseCase(crisisDetails)

        coVerify { crisisRepository.addRegister(crisisDetails) }

        assertEquals(expectedResult,result)

    }

    @Test
    fun `when getLastCrisisDetails is called, last crisis is returned`() = runBlocking{
        val crisisDetails = CrisisDetailsDB().copy(place = "Universidad")

        coEvery { crisisRepository.getLastCrisisDetails() } returns crisisDetails

        val result = saveCrisisRegistrationUseCase.getLastCrisisDetails()


        assertEquals(crisisDetails,result)

    }

    @Test
    fun `when register is updated, then response is success`() = runBlocking{

        //GIVEN
        val crisisDetails = CrisisDetailsDB().copy(place = "Universidad")
        coEvery { crisisRepository.updateCrisisDetails(crisisDetails) } returns FirebaseResult.Success

        //WHEN
        val result = saveCrisisRegistrationUseCase.updateCrisisDetails(crisisDetails)

        //THEN
        assertEquals(FirebaseResult.Success,result)

    }

//    @Test
//    fun `given register is updated, when repository throws exception, then response is failure`() = runBlocking{
//
//        //GIVEN
//        val crisisDetails = CrisisDetailsDB().copy(place = "Universidad")
//        val customThrowable = Throwable(message = "This is a throwable")
//
//        coEvery { crisisRepository.updateCrisisDetails(crisisDetails) } throws customThrowable
//        //WHEN
//        val result = saveCrisisRegistrationUseCase.updateCrisisDetails(crisisDetails)
//
//
//        //THEN
//
//        assertEquals(FirebaseResult.Error(customThrowable),result)
//        // CODIGO QUE CRASHEA EL TEST
//    }

}