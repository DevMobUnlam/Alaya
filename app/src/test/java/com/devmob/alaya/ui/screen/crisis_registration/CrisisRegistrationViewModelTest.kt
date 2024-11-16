
package com.devmob.alaya.ui.screen.crisis_registration

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.Observer
import com.devmob.alaya.domain.SaveCrisisRegistrationUseCase
import com.devmob.alaya.domain.model.CrisisBodySensation
import com.devmob.alaya.domain.model.CrisisDetails
import com.devmob.alaya.domain.model.CrisisDetailsDB
import com.devmob.alaya.domain.model.CrisisEmotion
import com.devmob.alaya.domain.model.CrisisPlace
import com.devmob.alaya.domain.model.CrisisTool
import com.devmob.alaya.domain.model.FirebaseResult
import com.devmob.alaya.domain.model.util.toDB
import com.devmob.alaya.utils.toCalendar
import com.devmob.alaya.utils.toDate
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.util.Calendar

private const val YEAR_MOCK = 2020
private const val MONTH_MOCK = 12
private const val DAY_MOCK = 15
private const val HOUR_MOCK = 13
private const val MINUTE_MOCK = 59

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class CrisisRegistrationViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @MockK
    private lateinit var saveCrisisRegistrationUseCase: SaveCrisisRegistrationUseCase

    private val dateMock: Calendar = Calendar.getInstance()

    private lateinit var viewModel: CrisisRegistrationViewModel

    @Before
    fun setUp() {
        dateMock.set(YEAR_MOCK, MONTH_MOCK, DAY_MOCK, HOUR_MOCK, MINUTE_MOCK)
        MockKAnnotations.init(this, relaxed = true)
        viewModel = CrisisRegistrationViewModel(saveCrisisRegistrationUseCase)
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @Test
    fun `when clean state is called, all states are reset`() {
        viewModel.cleanState()
        assertEquals(1, viewModel.screenState.value?.currentStep)
        assertTrue(viewModel.shouldGoToBack)
        assertFalse(viewModel.shouldGoToSummary)
    }

    @Test
    fun `when goOneStepForward is called, currentStep is incremented`() {
        viewModel.goOneStepForward()
        assertEquals(2, viewModel.screenState.value?.currentStep)
    }

    @Test
    fun `when goOneStepBack is called, currentStep is decremented`() {
        viewModel.goOneStepBack()
        assertEquals(0, viewModel.screenState.value?.currentStep)
    }

    @Test
    fun `when updateStep is called, currentStep is updated`() {
        viewModel.updateStep(3)
        assertEquals(3, viewModel.screenState.value?.currentStep)
    }

    @Test
    fun `when addCrisisPlace is called, place is added to the list`() {
        val mockkIcon = mockk<ImageVector>()
        val place = CrisisPlace("Calle", mockkIcon)
        viewModel.addCrisisPlace(place)
        assertEquals(5, viewModel.places.value?.size)
    }

    @Test
    fun `when addCrisisPlace is called and that place exists, the place is not added to the list`() {
        val mockkIcon = mockk<ImageVector>()
        val place = CrisisPlace("Casa", mockkIcon)
        viewModel.addCrisisPlace(place)
        viewModel.addCrisisPlace(place)
        assertEquals(4, viewModel.places.value?.size)
    }

    @Test
    fun `when addCrisisTool is called, tool is added to the list`() {
        val mockkIcon = mockk<ImageVector>()
        val tool = CrisisTool("Meditacion", "", mockkIcon)
        viewModel.addCrisisTool(tool)
        assertEquals(4, viewModel.tools.value?.size)
    }

    @Test
    fun `when addCrisisTool is called and that tool exists, the tool is not added to the list`() {
        val mockkIcon = mockk<ImageVector>()
        val tool = CrisisTool("Respiracion", "", mockkIcon)
        val observer = Observer<List<CrisisTool>> {}

        viewModel.tools.observeForever(observer)
        viewModel.addCrisisTool(tool)
        viewModel.addCrisisTool(tool)

        assertEquals(4, viewModel.tools.value?.size)
    }

    @Test
    fun `when addCrisisBodySensation is called, body sensation is added to the list`() {
        val mockkIcon = mockk<ImageVector>()
        val bodySensation = CrisisBodySensation("Desmayo", mockkIcon)
        val observer = Observer<List<CrisisBodySensation>> {}

        viewModel.bodySensations.observeForever(observer)
        viewModel.addCrisisBodySensation(bodySensation)

        assertEquals(4, viewModel.bodySensations.value?.size)
    }

    @Test
    fun `when addCrisisBodySensation is called and that body sensation exits, the body sensation is not added to the list`() {
        val mockkIcon = mockk<ImageVector>()
        val bodySensation = CrisisBodySensation("Temblores", mockkIcon)
        viewModel.addCrisisBodySensation(bodySensation)
        assertEquals(3, viewModel.bodySensations.value?.size)
    }

    @Test
    fun `when addCrisisEmotion is called, emotion is added to the list`() {
        val mockkIcon = mockk<ImageVector>()
        val emotion = CrisisEmotion("Angustia", mockkIcon)
        viewModel.addCrisisEmotion(emotion)
        assertEquals(4, viewModel.emotions.value?.size)
    }

    @Test
    fun `when addCrisisEmotion is called and that emotion exits, the emotion is not added to the list`() {
        val mockkIcon = mockk<ImageVector>()
        val emotion = CrisisEmotion("Tristeza", mockkIcon)
        viewModel.addCrisisEmotion(emotion)
        assertEquals(3, viewModel.emotions.value?.size)
    }

    @Test
    fun `when updateCrisisBodySensation is called, then update list`() {
        val mockkIcon = mockk<ImageVector>()
        val bodySensation1 = CrisisBodySensation("Desmayo", mockkIcon)
        val bodySensation2 = CrisisBodySensation("Calor", mockkIcon)
        viewModel.selectCrisisBodySensation(bodySensation1)
        viewModel.selectCrisisBodySensation(bodySensation2)
        assertEquals(2, viewModel.screenState.value?.crisisDetails?.bodySensationList?.size)
    }

    @Test
    fun `when updateCrisisEmotion is called, then update list`() {
        val mockkIcon = mockk<ImageVector>()
        val emotion1 = CrisisEmotion("Angustia", mockkIcon)
        val emotion2 = CrisisEmotion("Tristeza", mockkIcon)
        viewModel.updateCrisisEmotion(emotion1)
        viewModel.updateCrisisEmotion(emotion2)
        assertEquals(2, viewModel.screenState.value?.crisisDetails?.emotionList?.size)
    }

    @Test
    fun `when updateCrisisTool is called, then update list`() {
        val mockkIcon = mockk<ImageVector>()
        val observer = Observer<CrisisRegistrationScreenState> {}
        val tool1 = CrisisTool("Meditacion", "", mockkIcon)
        val tool2 = CrisisTool("Respiracion", "", mockkIcon)
        viewModel.screenState.observeForever(observer)
        viewModel.updateCrisisTool(tool1)
        viewModel.updateCrisisTool(tool2)
        assertEquals(0, viewModel.screenState.value?.crisisDetails?.toolList?.size)
    }

    @Test
    fun `when updateNotes is called, then update notes`() {
        val notes = "Notas"
        viewModel.updateNotes(notes)
        assertEquals(notes, viewModel.screenState.value?.crisisDetails?.notes)
    }

    @Test
    fun `when updatePlace is called, then update place`() {
        val mockkIcon = mockk<ImageVector>()
        val place = CrisisPlace("Casa", mockkIcon)
        viewModel.updatePlace(place, 0)
        assertEquals(place, viewModel.screenState.value?.crisisDetails?.placeList?.get(0))
    }

    @Test
    fun `when clearPlaceSelection is called, then clear list`() {
        val mockkIcon = mockk<ImageVector>()
        val place = CrisisPlace("Casa", mockkIcon)
        viewModel.addCrisisPlace(place)
        viewModel.clearPlaceSelection()
        assertEquals(0, viewModel.screenState.value?.crisisDetails?.placeList?.size)
    }

    @Test
    fun `when dismissExitModal is called, then shouldShowExitModal is false`() {
        viewModel.dismissExitModal()
        assertFalse(viewModel.shouldShowExitModal)
    }

    @Test
    fun `when showExitModal is called, then shouldShowExitModal is true`() {
        viewModel.showExitModal()
        assertTrue(viewModel.shouldShowExitModal)
    }

    @Test
    fun `when updateStartDate is called, then update starting date`() {
        viewModel.updateStartDate(dateMock.toDate())

        val actual = viewModel.screenState.value?.crisisDetails?.crisisTimeDetails?.startTime
        assertEquals(
            dateMock.get(Calendar.YEAR),
            actual?.toCalendar()?.get(Calendar.YEAR)
        )
        assertEquals(
            dateMock.get(Calendar.MONTH),
            actual?.toCalendar()?.get(Calendar.MONTH)
        )
        assertEquals(
            dateMock.get(Calendar.DAY_OF_MONTH),
            actual?.toCalendar()?.get(Calendar.DAY_OF_MONTH)
        )
    }

    @Test
    fun `when updateStartTime is called, then update start time`() {

        viewModel.updateStartTime(dateMock.toDate())
        val actual = viewModel.screenState.value?.crisisDetails?.crisisTimeDetails?.startTime
        assertEquals(
            dateMock.get(Calendar.HOUR_OF_DAY),
            actual?.toCalendar()?.get(Calendar.HOUR_OF_DAY)
        )
        assertEquals(
            dateMock.get(Calendar.MINUTE),
            actual?.toCalendar()?.get(Calendar.MINUTE)
        )
    }

    @Test
    fun `when updateEndDate is called, then update end date`() {
        viewModel.updateEndDate(dateMock.toDate())

        val actual = viewModel.screenState.value?.crisisDetails?.crisisTimeDetails?.endTime
        assertEquals(
            dateMock.get(Calendar.YEAR),
            actual?.toCalendar()?.get(Calendar.YEAR)
        )
        assertEquals(
            dateMock.get(Calendar.MONTH),
            actual?.toCalendar()?.get(Calendar.MONTH)
        )
        assertEquals(
            dateMock.get(Calendar.DAY_OF_MONTH),
            actual?.toCalendar()?.get(Calendar.DAY_OF_MONTH)
        )
    }

    @Test
    fun `when updateEndTime is called, then update end time`() {
        viewModel.updateEndTime(dateMock.toDate())
        val actual = viewModel.screenState.value?.crisisDetails?.crisisTimeDetails?.endTime
        assertEquals(
            dateMock.get(Calendar.HOUR_OF_DAY),
            actual?.toCalendar()?.get(Calendar.HOUR_OF_DAY)
        )
        assertEquals(
            dateMock.get(Calendar.MINUTE),
            actual?.toCalendar()?.get(Calendar.MINUTE)
        )
    }

    @Test
    fun `when hideBackButton is called, shouldGoToBack is false and shouldGoToSummary is true`() {
        viewModel.hideBackButton()
        assertFalse(viewModel.shouldGoToBack)
        assertTrue(viewModel.shouldGoToSummary)
    }

    @Test
    fun `when saveRegister is called, then call getLastCrisisDetails`() {
        mockkStatic("com.devmob.alaya.domain.model.util.MappersToDBKt")
        val expected = CrisisDetailsDB()

        every { any<CrisisDetails>().toDB() } returns expected
        coEvery { saveCrisisRegistrationUseCase(expected) } returns FirebaseResult.Success

        viewModel.saveRegister()

        coVerify { saveCrisisRegistrationUseCase.getLastCrisisDetails() }
    }
}
