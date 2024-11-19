package com.devmob.alaya.data

import android.util.Log
import com.devmob.alaya.domain.GetUserRepository
import com.devmob.alaya.domain.model.Invitation
import com.devmob.alaya.domain.model.InvitationStatus
import com.devmob.alaya.domain.model.User
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.WriteBatch
import io.mockk.Awaits
import io.mockk.MockKAnnotations
import io.mockk.MockKStubScope
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class GetUserRepositoryImplTest {

    @MockK
    private lateinit var firebaseClient: FirebaseClient

    @MockK
    private lateinit var exceptionMock: Exception

    @MockK
    private lateinit var successMock: Task<DocumentSnapshot>

    @MockK
    private lateinit var successVoid: Void

    @MockK
    private lateinit var successTaskMock: Task<Void>

    @MockK
    private lateinit var snapshotMock: DocumentSnapshot

    @MockK
    private lateinit var firebaseDB: FirebaseFirestore

    @MockK
    private lateinit var patientRef: DocumentReference

    @MockK
    private lateinit var professionalRef: DocumentReference


    private lateinit var repository: GetUserRepository


    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        patientRef = mockk()
        professionalRef = mockk()

        coEvery { firebaseClient.db } returns firebaseDB
        every { successMock.isSuccessful } returns true
        every { successMock.result } returns snapshotMock
        every { snapshotMock.toObject(User::class.java) } returns User(email = "email")
        coEvery { firebaseDB.collection("users").document("email").get() } returns successMock
        mockkStatic("kotlinx.coroutines.tasks.TasksKt")
        coEvery { successMock.await() } returns snapshotMock
        coEvery { successTaskMock.await() } returns successVoid

        repository = GetUserRepositoryImpl(firebaseClient)
    }

    @Test
    fun `given an email, when getUser is called, then return User`() =
        runBlocking {
            //GIVEN
            val email = "email"

            //WHEN
            val result = repository.getUser(email)

            //THEN
            assertEquals(email, result?.email)
        }

    @Test
    fun `given an incorrect email, when getUser is called, then return null`() =
        runBlocking {
            //GIVEN
            val email = "incorrectEmail"

            //WHEN
            val result = repository.getUser(email)

            //THEN
            assertEquals(null, result?.email)
        }

    @Test
    fun `given an existing field and a new value, when updateUserField is called, then update field on database`() =
        runBlocking {
            //GIVEN
            val email = "email"
            val existingField = "field"
            val newValue = "value"

            coEvery {
                firebaseDB.collection("users").document("email").update("field", "value")
            } returns successTaskMock

            //WHEN
            repository.updateUserField(email, existingField, newValue)

            coVerify { firebaseDB.collection("users").document("email").update("field", "value") }
        }

    @Test
    fun `given a field and a value, when addNewField is called, then set new field on database`() =
        runBlocking {
            //GIVEN
            val email = "email"
            val newField = "newField"
            val value = "content"

            coEvery {
                firebaseDB.collection("users").document("email")
                    .set(mapOf("newField" to "content"), SetOptions.merge())
            } returns successTaskMock

            //WHEN
            repository.addNewField(email, newField, value)

            coVerify {
                firebaseDB.collection("users").document("email")
                    .set(mapOf("newField" to "content"), SetOptions.merge())
            }
        }

    //TODO Testear sendInvitation. No logré mockear el db.runbatch
/*
    @Test
    fun `given valid patient and professional invitations, when sendInvitation is called, then return success`() =
        runBlocking {
            //GIVEN
            mockkStatic(Log::class)
            every { Log.d(any(), any()) } returns 0
            val patientInvitation = Invitation("patientEmail", InvitationStatus.PENDING)
            val professionalInvitation = Invitation("professionalEmail", InvitationStatus.PENDING)

            //WHEN
            val result = repository.sendInvitation(patientInvitation, professionalInvitation)

            //THEN
            assertEquals(Result.success(Unit), result)

        }*/
}

