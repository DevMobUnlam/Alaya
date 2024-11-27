package com.devmob.alaya.domain

import com.devmob.alaya.domain.model.Session
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.devmob.alaya.data.SessionRepositoryImpl
import com.devmob.alaya.domain.model.FirebaseResult
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class SessionUseCaseTest {

    private lateinit var sessionUseCase: SessionUseCase

    @MockK
    private lateinit var notificationRepository: NotificationRepository

    @MockK
    private lateinit var sessionRepository: SessionRepository

    @MockK
    private lateinit var sessionMock: Session

    @MockK
    private lateinit var listMock: List<Session>

    @MockK
    private lateinit var successMock: Response<Unit>

    @MockK
    private lateinit var errorMock: Response<Unit>

    private val testDispatcher = UnconfinedTestDispatcher()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
        Dispatchers.setMain(testDispatcher)
        coEvery {
            sessionRepository.addSession(
                sessionMock,
                "email"
            )
        } returns FirebaseResult.Success

        coEvery { sessionRepository.getSessions("email") } returns listMock
        coEvery { sessionRepository.getNextSession("email") } returns sessionMock
        coEvery { sessionRepository.getNextSession("invalidEmail") } returns null
        coEvery { notificationRepository.sendNotificationSessions("email") } returns successMock
        coEvery { notificationRepository.sendNotificationSessions("invalidEmail") } returns errorMock
        every { errorMock.isSuccessful } returns false

        sessionUseCase = SessionUseCase(notificationRepository, sessionRepository)
    }

    @Test
    fun `given a valid email and session, when invoke is called, then return success`() =
        runBlocking {
            //GIVEN
            val email = "email"

            //WHEN
            val result = sessionUseCase(sessionMock, email)
            val expected = FirebaseResult.Success

            //THEN
            assertEquals(expected, result)
            coVerify { sessionRepository.addSession(sessionMock, email) }
        }

    @Test
    fun `given an invalid email and a session, when invoke is called, then return Error`() =
        runBlocking {
            //GIVEN
            val invalidEmail = "invalidEmail"

            //WHEN
            val result = sessionUseCase(sessionMock, invalidEmail)

            //THEN
            assert(result is FirebaseResult.Error)
            coVerify { sessionRepository.addSession(sessionMock, invalidEmail) }
        }


    @Test
    fun `given a valid email, when getSessions is called, then return a list`(): Unit =
        runBlocking {
            //GIVEN
            val email = "email"

            //WHEN
            val list = sessionUseCase.getSessions(email)

            //THEN
            assert(list.isNotEmpty())
            coVerify { sessionRepository.getSessions(email) }
        }

    @Test
    fun `given an invalid email, when getSessions is called, then return a empty list`(): Unit =
        runBlocking {
            //GIVEN
            val invalidEmail = "invalidEmail"

            //WHEN
            val list = sessionUseCase.getSessions(invalidEmail)

            //THEN
            assert(list.isEmpty())
            coVerify { sessionRepository.getSessions(invalidEmail) }
        }

    @Test
    fun `given a valid email, when getNextSession is called, then return a Session`(): Unit =
        runBlocking {
            //GIVEN
            val email = "email"

            //WHEN
            val session = sessionUseCase.getNextSession(email)

            //THEN
            assertEquals(sessionMock, session)
            coVerify { sessionRepository.getNextSession(email) }
        }

    @Test
    fun `given an invalid email, when getNextSession is called, then return null`(): Unit =
        runBlocking {
            //GIVEN
            val invalidEmail = "invalidEmail"

            //WHEN
            val nullSession = sessionUseCase.getNextSession(invalidEmail)

            //THEN
            assertEquals(null, nullSession)
            coVerify { sessionRepository.getNextSession(invalidEmail) }
        }


    @Test
    fun `given a valid email, when sendNotificationSession is called, then return Success`() = runBlocking {
        //GIVEN
        val email = "email"

        //WHEN
        val result = sessionUseCase.sendNotificationSession(email)

        //THEN
        assertEquals(successMock, result)
        coVerify { notificationRepository.sendNotificationSessions(email) }
    }

    @Test
    fun `given an invalid email, when sendNotificationSession is called, then return Error`() = runBlocking {
        //GIVEN
        val invalidEmail = "invalidEmail"

        //WHEN
        val result = sessionUseCase.sendNotificationSession(invalidEmail)

        //THEN
        assertEquals(errorMock, result)
        coVerify { notificationRepository.sendNotificationSessions(invalidEmail) }
    }
}