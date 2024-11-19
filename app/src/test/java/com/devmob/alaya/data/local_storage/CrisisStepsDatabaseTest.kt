package com.devmob.alaya.data.local_storage

import android.content.Context
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertSame
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class CrisisStepsDatabaseTest {

    @MockK
    private lateinit var context: Context

    private lateinit var database: CrisisStepsDatabase

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
    }

    @Test
    fun `when getDataBase is called, then it should return an instance of CrisisStepsDatabase`(){
        database = CrisisStepsDatabase.getDataBase(context)
        val dao = database.crisisStepsDao()
        assertNotNull(dao)
    }

    @Test
    fun `when getDataBase is called twice, then it should return the same instance of CrisisStepsDatabase`(){
        val database1 = CrisisStepsDatabase.getDataBase(context)
        val database2 = CrisisStepsDatabase.getDataBase(context)
        assertSame(database1, database2)
    }
}