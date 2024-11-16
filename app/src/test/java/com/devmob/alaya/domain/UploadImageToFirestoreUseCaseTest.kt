package com.devmob.alaya.domain

import android.net.Uri
import com.devmob.alaya.data.preferences.SharedPreferences
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class UploadImageToFirestoreUseCaseTest {

    private lateinit var useCase: UploadImageToFirestoreUseCase

    @MockK
    private lateinit var repositoryMockk: UploadImageToFirestoreRepository

    @MockK
    private lateinit var prefsMockk: SharedPreferences

    @Before
    fun setUp() {
        MockKAnnotations.init(this, true)
        every { prefsMockk.getEmail() } returns ""
        useCase = UploadImageToFirestoreUseCase(repositoryMockk, prefsMockk)
    }

    @Test
    fun `When upload image and repository return uri then use case return same uri`() = runTest {
        val expected = mockk<Uri>()
        coEvery { repositoryMockk.uploadImage(any(), any()) } returns expected

        val result = useCase.invoke("imageUrl")

        coVerify { repositoryMockk.uploadImage(any(), any()) }
        Assert.assertEquals(expected, result)
    }

    @Test
    fun `when upload image and repository return null then use case return null`() = runTest {
        val expected = null
        coEvery { repositoryMockk.uploadImage(any(), any()) } returns expected

        val result = useCase.invoke("")

        coVerify { repositoryMockk.uploadImage(any(), any()) }
        Assert.assertEquals(expected, result)
    }
}