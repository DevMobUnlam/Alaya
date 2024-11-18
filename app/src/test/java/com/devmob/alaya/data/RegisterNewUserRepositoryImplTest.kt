package com.devmob.alaya.data

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

class RegisterNewUserRepositoryImplTest {

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


    private lateinit var registerNewUserRepositoryImpl: RegisterNewUserRepositoryImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
        coEvery { successMock.isComplete } returns true
        coEvery { successMock.exception } returns null
        coEvery { successMock.isCanceled } returns false
        coEvery { firebaseClient.auth } returns firebaseAuth
        coEvery { authResultMock.user } returns firebaseUserMock
        coEvery { successMock.result } returns authResultMock
        coEvery { failureMock.isComplete } returns true
        coEvery { failureMock.exception } returns exceptionMock
        coEvery { firebaseUserMock.uid } returns "mockedUid"
        coEvery { firebaseUserMock.email } returns "user@gmail.com"

        mockkStatic("kotlinx.coroutines.tasks.TasksKt")
        registerNewUserRepositoryImpl = RegisterNewUserRepositoryImpl(firebaseClient)
    }

    @Test
    fun `given user and password when createUserWithEmailAndPassword is called then return AuthenticationResult success`() =
        runTest {
            // GIVEN
            val user = "user@gmail.com"
            val password = "password123"
            val authenticationResult = AuthenticationResult.Success
            coEvery {
                firebaseAuth.createUserWithEmailAndPassword(
                    user,
                    password
                )
            } returns successMock

            // WHEN
            val result =
                registerNewUserRepositoryImpl.createUserWithEmailAndPassword(user, password)

            // THEN
            assertEquals(authenticationResult, result)
        }

    @Test
    fun `given user and password when createUserWithEmailAndPassword is called then return AuthenticationResult Error`() =
        runTest {
            // GIVEN
            val user = "user@gmail.com"
            val password = "password123"
            val authenticationResult = AuthenticationResult.Error(exceptionMock)
            coEvery {
                firebaseAuth.createUserWithEmailAndPassword(
                    user,
                    password
                )
            } returns failureMock

            // WHEN
            val result =
                registerNewUserRepositoryImpl.createUserWithEmailAndPassword(user, password)

            // THEN
            assertEquals(authenticationResult, result)
        }
}
