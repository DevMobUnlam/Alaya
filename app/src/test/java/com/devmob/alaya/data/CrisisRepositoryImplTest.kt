package com.devmob.alaya.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.devmob.alaya.domain.CrisisRepository
import com.devmob.alaya.domain.model.CrisisDetailsDB
import com.devmob.alaya.domain.model.FirebaseResult
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirestoreRegistrar
import com.google.firebase.firestore.QuerySnapshot
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CrisisRepositoryImplTest{
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var crisisRepository: CrisisRepository

    @MockK
    private lateinit var firebaseClient: FirebaseClient

    @MockK
    private lateinit var successMock: DocumentReference

    @MockK
    private lateinit var querySnapshotMockK: QuerySnapshot

    @MockK
    private lateinit var firebaseAuth: FirebaseAuth

    @MockK
    private lateinit var db: FirebaseFirestore


    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        MockKAnnotations.init(this,relaxed = true)
        Dispatchers.setMain(testDispatcher)
        crisisRepository = CrisisRepositoryImpl(firebaseClient)
        coEvery { firebaseClient.auth } returns firebaseAuth
        coEvery { firebaseClient.db } returns db
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
    fun `when crisis is added correctly, response is Success`() = runBlocking {
        //GIVEN
        val register = CrisisDetailsDB().copy(place = "Casa")
        //WHEN

        val expectedResult = FirebaseResult.Success


        coEvery{firebaseClient.auth.currentUser?.email?.let {
            firebaseClient
                .db
                .collection("users")
                .document(any())
                .collection("crisis_registers")
                .add(register)
                .await()
        }} returns successMock

        val result = crisisRepository.addRegister(register)

        //THEN
        Assert.assertEquals(expectedResult,result)
    }

//    @Test
//    fun `given registers exist, when getRegisters is called, response is not empty`() = runBlocking {
//        //GIVEN
//        val register = CrisisDetailsDB().copy(place = "Casa")
//
//        val patientID = "email@test.com"
//
//        val documentSnapshotMockK = mockk<DocumentSnapshot>()
//        val querySnapshotMock = mockk<QuerySnapshot>()
//        val taskMock = mockk<Task<QuerySnapshot>>()
//
//        coEvery { querySnapshotMock.documents } returns mutableListOf(documentSnapshotMockK)
//        coEvery { taskMock.isSuccessful } returns true
//        coEvery { taskMock.result } returns querySnapshotMock
//
//        coEvery { taskMock.addOnSuccessListener(any()) } returns taskMock
//
//        coEvery{db
//            .collection("users")
//            .document(patientID)
//            .collection("crisis_registers")
//            .orderBy("start",any())
//            .limit(1)
//            .get()
//        } returns taskMock
//
//
//        //WHEN
//        val result = crisisRepository.getRegisters(patientID){}.first()?.first()
//
//        //THEN
//        Assert.assertEquals(register,result)
//    }

    //    @Test
//    fun `given no registers exist, when getRegisters is called, response is empty`() = runBlocking {
//        //GIVEN
//        val register = CrisisDetailsDB().copy(place = "Casa")
//        val patientID = "email@test.com"
//
//        //WHEN
//        coEvery{db
//                .collection("users")
//                .document(any())
//                .collection("crisis_registers")
//                .orderBy("start",any())
//                .limit(1)
//                .get()
//                .addOnSuccessListener {  }
//        } returns flow{}
//
//        val result = crisisRepository.getRegisters("email@test.com") {}.first()?.first()
//
//        //THEN
//        Assert.assertEquals(register,result)
//    }

    //TODO GETLASTCRISISDETAILS - UPDATECRISISDETAILS

}