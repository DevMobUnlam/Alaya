package com.devmob.alaya.ui.screen.crisis_registration

import androidx.compose.ui.graphics.vector.ImageVector
import com.devmob.alaya.domain.model.CrisisBodySensation
import com.devmob.alaya.domain.model.CrisisEmotion
import com.devmob.alaya.domain.model.CrisisPlace
import com.devmob.alaya.domain.model.CrisisTool
import io.mockk.MockKAnnotations
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.util.Date

@RunWith(RobolectricTestRunner::class)
class CrisisRegistrationViewModelTest {

    private lateinit var viewModel: CrisisRegistrationViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
        viewModel = CrisisRegistrationViewModel()
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
        // TODO modificar test cuando se consulte el repositorio
        assertEquals(5, viewModel.places.value?.size)
    }

    @Test
    fun `when addCrisisPlace is called and that place exists, the place is not added to the list`() {
        val mockkIcon = mockk<ImageVector>()
        val place = CrisisPlace("Casa", mockkIcon)
        viewModel.addCrisisPlace(place)
        viewModel.addCrisisPlace(place)
        // TODO modificar test cuando se consulte el repositorio
        assertEquals(4, viewModel.places.value?.size)
    }

    @Test
    fun `when addCrisisTool is called, tool is added to the list`() {
        val mockkIcon = mockk<ImageVector>()
        val tool = CrisisTool("Meditacion", mockkIcon)
        viewModel.addCrisisTool(tool)
        // TODO modificar test cuando se consulte el repositorio
        assertEquals(4, viewModel.tools.value?.size)
    }

    @Test
    fun `when addCrisisTool is called and that tool exists, the tool is not added to the list`() {
        val mockkIcon = mockk<ImageVector>()
        val tool = CrisisTool("Respiracion", mockkIcon)
        viewModel.addCrisisTool(tool)
        viewModel.addCrisisTool(tool)
        // TODO modificar test cuando se consulte el repositorio
        assertEquals(3, viewModel.tools.value?.size)
    }

    @Test
    fun `when addCrisisBodySensation is called, body sensation is added to the list`() {
        val mockkIcon = mockk<ImageVector>()
        val bodySensation = CrisisBodySensation("Desmayo", mockkIcon)
        viewModel.addCrisisBodySensation(bodySensation)
        // TODO modificar test cuando se consulte el repositorio
        assertEquals(4, viewModel.bodySensations.value?.size)
    }

    @Test
    fun `when addCrisisBodySensation is called and that body sensation exits, the body sensation is not added to the list`() {
        val mockkIcon = mockk<ImageVector>()
        val bodySensation = CrisisBodySensation("Temblores", mockkIcon)
        viewModel.addCrisisBodySensation(bodySensation)
        // TODO modificar test cuando se consulte el repositorio
        assertEquals(3, viewModel.bodySensations.value?.size)
    }

    @Test
    fun `when addCrisisEmotion is called, emotion is added to the list`() {
        val mockkIcon = mockk<ImageVector>()
        val emotion = CrisisEmotion("Angustia", mockkIcon)
        viewModel.addCrisisEmotion(emotion)
        // TODO modificar test cuando se consulte el repositorio
        assertEquals(4, viewModel.emotions.value?.size)
    }

    @Test
    fun `when addCrisisEmotion is called and that emotion exits, the emotion is not added to the list`() {
        val mockkIcon = mockk<ImageVector>()
        val emotion = CrisisEmotion("Tristeza", mockkIcon)
        viewModel.addCrisisEmotion(emotion)
        // TODO modificar test cuando se consulte el repositorio
        assertEquals(3, viewModel.emotions.value?.size)
    }

    @Test
    fun `when updateCrisisBodySensation is called, then update list`() {
        val mockkIcon = mockk<ImageVector>()
        val bodySensation1 = CrisisBodySensation("Desmayo", mockkIcon)
        val bodySensation2 = CrisisBodySensation("Calor", mockkIcon)
        viewModel.updateCrisisBodySensation(bodySensation1)
        viewModel.updateCrisisBodySensation(bodySensation2)
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
        val tool1 = CrisisTool("Meditacion", mockkIcon)
        val tool2 = CrisisTool("Respiracion", mockkIcon)
        viewModel.updateCrisisTool(tool1)
        viewModel.updateCrisisTool(tool2)
        assertEquals(2, viewModel.screenState.value?.crisisDetails?.toolList?.size)
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
        val date = mockk<Date>()
        viewModel.updateStartDate(date)
        assertEquals(
            date,
            viewModel.screenState.value?.crisisDetails?.crisisTimeDetails?.startingDate
        )
    }

    @Test
    fun `when updateStartTime is called, then update start time`() {
        val time = mockk<Date>()
        viewModel.updateStartTime(time)
        assertEquals(
            time,
            viewModel.screenState.value?.crisisDetails?.crisisTimeDetails?.startTIme
        )
    }

    @Test
    fun `when updateEndDate is called, then update end date`() {
        val date = mockk<Date>()
        viewModel.updateEndDate(date)
        assertEquals(
            date,
            viewModel.screenState.value?.crisisDetails?.crisisTimeDetails?.endDate
        )
    }

    @Test
    fun `when updateEndTime is called, then update end time`() {
        val time = mockk<Date>()
        viewModel.updateEndTime(time)
        assertEquals(
            time,
            viewModel.screenState.value?.crisisDetails?.crisisTimeDetails?.endTime
        )
    }

    @Test
    fun `when hideBackButton is called, shouldGoToBack is false and shouldGoToSummary is true`() {
        viewModel.hideBackButton()
        assertFalse(viewModel.shouldGoToBack)
        assertTrue(viewModel.shouldGoToSummary)
    }
}
