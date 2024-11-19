package com.devmob.alaya.data

import com.devmob.alaya.domain.LoginRepository
import com.devmob.alaya.domain.model.AuthenticationResult
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockkStatic
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class LoginRepositoryImplTest {
    @MockK
    private lateinit var firebaseClient: FirebaseClient

    @MockK
    private lateinit var successMock: Task<AuthResult>

    @MockK
    private lateinit var failureMock: Task<AuthResult>

    @MockK
    private lateinit var exceptionMock: Exception

    @MockK
    private lateinit var firebaseAuth: FirebaseAuth

    @MockK
    private lateinit var authResultMock: AuthResult

    @MockK
    private lateinit var firebaseUserMock: FirebaseUser

    private lateinit var repository: LoginRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
        coEvery { successMock.isComplete } returns true
        coEvery { successMock.exception } returns null
        coEvery { successMock.isCanceled } returns false
        coEvery { firebaseClient.auth } returns firebaseAuth
        coEvery { successMock.result } returns authResultMock
        coEvery { failureMock.isComplete } returns true
        coEvery { failureMock.exception } returns exceptionMock
        mockkStatic("kotlinx.coroutines.tasks.TasksKt")

        repository = LoginRepositoryImpl(firebaseClient)
    }

    @Test
    fun `given user and password, when signInWithEmailAndPassword is called, then return AuthenticationResult success`() =
        runTest {
            //GIVEN
            val user = "user@gmail.com"
            val password = "password123"
            val authenticationResult = AuthenticationResult.Success

            coEvery {
                firebaseAuth.signInWithEmailAndPassword(user, password)
            } returns successMock

            //WHEN
            val result = repository.login(user, password)

            //THEN
            assertEquals(authenticationResult, result)
        }

    @Test
    fun `given user and password, when signInWithEmailAndPassword is called, then return AuthenticationResult error`() =
        runTest {
            //GIVEN
            val user = "user@gmail.com"
            val password = "password123"
            val authenticationResult = AuthenticationResult.Error(exceptionMock)

            coEvery {
                firebaseAuth.signInWithEmailAndPassword(user, password)
            } returns failureMock

            //WHEN
            val result = repository.login(user, password)

            //THEN
            assertEquals(authenticationResult, result)
        }
}