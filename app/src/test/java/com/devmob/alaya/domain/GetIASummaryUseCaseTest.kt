package com.devmob.alaya.domain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.devmob.alaya.data.FirebaseClient
import com.devmob.alaya.data.mapper.toIASummaryModel
import com.devmob.alaya.domain.model.CrisisDetailsDB
import com.devmob.alaya.domain.model.FirebaseResult
import com.devmob.alaya.domain.model.User
import com.devmob.alaya.ui.screen.searchUser.SearchUserViewModel
import com.google.ai.client.generativeai.GenerativeModel
import com.google.gson.Gson
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock

class GetIASummaryUseCaseTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()


    @MockK
    private lateinit var gson:Gson

    @MockK
    private lateinit var crisisRepository: CrisisRepository

    private lateinit var getIASummaryUseCase: GetIASummaryUseCase

    @MockK
    private lateinit var getUserRepository: GetUserRepository

    @MockK
    private lateinit var generativeModel: GenerativeModel



    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        MockKAnnotations.init(this,relaxed = true)
        Dispatchers.setMain(testDispatcher)
        getIASummaryUseCase = GetIASummaryUseCase(gson,generativeModel,crisisRepository,getUserRepository)
        mockkStatic("kotlinx.coroutines.tasks.TasksKt")

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
        unmockkAll()
    }

    @Test
    fun `should generate summary when registers exist`()= runBlocking {
        //GIVEN
        val register = CrisisDetailsDB().copy(place = "Casa")

        val patientId = "patient123@test.com"
        val instructions = "Summarize"
        val registerList = listOf(register)
        val mappedList = registerList.map { it.toIASummaryModel() }
        val jsonOutput = "mockJson"
        val generatedText = "Generated Summary"

        coEvery { getUserRepository.getUser(patientId) } returns User(email = patientId, name = "TestPatient")
        coEvery { crisisRepository.getRegisters(patientId,any()) } returns flow { emit(registerList) }
        coEvery { gson.toJson(any())} returns jsonOutput
        coEvery { generativeModel.generateContent(any<String>()).text } returns generatedText

        //WHEN
        val result = getIASummaryUseCase(instructions = instructions, patientId = patientId, onRegisterUpdate = {}).first()

        assertEquals(generatedText, result)
    }



    //TODO si no existen registros de crisis

    @Test
    fun `should emit empty summary when no registers exist`()= runBlocking {
        //GIVEN
        val patientId = "patient123@test.com"
        val instructions = "Summarize"


        coEvery { getUserRepository.getUser(patientId) } returns User(email = patientId, name = "TestPatient")
        coEvery { crisisRepository.getRegisters(patientId,any()) } returns flow { emit(emptyList()) }


        //WHEN
        val result = getIASummaryUseCase(instructions = instructions, patientId = patientId, onRegisterUpdate = {}).first()

        assertEquals("", result)
    }

}