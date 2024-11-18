package com.devmob.alaya.data

import com.devmob.alaya.domain.UserFirestoreRepository
import com.devmob.alaya.domain.model.FirebaseResult
import com.devmob.alaya.domain.model.User
import com.google.android.gms.tasks.Task
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class UserFirestoreRepositoryImplTest {
    @MockK
    private lateinit var firebaseClient: FirebaseClient

    @MockK
    private lateinit var userMockk: User

    @MockK
    private lateinit var successMock: Task<Void>

    @MockK
    private lateinit var failureMock: Task<Void>

    @MockK
    private lateinit var exceptionMock: Exception

    private lateinit var repository: UserFirestoreRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
        coEvery { userMockk.email } returns "test@test.com"
        coEvery { successMock.isComplete } returns true
        coEvery { successMock.exception } returns null
        coEvery { successMock.isCanceled } returns false
        coEvery { successMock.result } returns mockk()
        coEvery { failureMock.isComplete } returns true
        coEvery { failureMock.exception } returns exceptionMock

        mockkStatic("kotlinx.coroutines.tasks.TasksKt")
        repository = UserFirestoreRepositoryImpl(firebaseClient)
    }

    @Test
    fun `When a user is added and it is Success then it is created successfully`(): Unit =
        runBlocking {
            coEvery {
                firebaseClient.db.collection("users").document(any()).set(any())
            } returns successMock

            val expected = FirebaseResult.Success
            val result = repository.addUser(userMockk)
            Assert.assertEquals(expected, result)
        }

    @Test
    fun `when adding a user and it fails then it returns an error`(): Unit = runBlocking {
        coEvery {
            firebaseClient.db.collection("users").document(any()).set(any())
        } returns failureMock
        val expected = FirebaseResult.Error(exceptionMock)
        val result = repository.addUser(userMockk)
        Assert.assertEquals(expected, result)
    }
}
