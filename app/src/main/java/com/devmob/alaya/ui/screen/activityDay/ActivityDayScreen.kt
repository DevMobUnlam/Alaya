package com.devmob.alaya.ui.screen.activityDay

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.size.Dimension
import com.devmob.alaya.domain.model.DailyActivity
import com.devmob.alaya.ui.theme.ColorText

/**
 * Pantalla que muestra las actividades diarias
 */
@Composable
fun ActivityDayScreen(viewModel: ActivityDayViewModel = hiltViewModel()) {

    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    /*LaunchedEffect(Unit){
        if(!viewModel.viewModelScope.isActive){
            viewModel.loadDailyActivities()
        }
    }*/

    val listOfActivities = listOf(
        DailyActivity(title = "Actividad #1", description = "Esta es la descripcion", currentProgress = 2, maxProgress = 5, isChecked = true),
        DailyActivity(title = "Actividad #2", description = "Esta es la descripcion", currentProgress = 2, maxProgress = 5, isChecked = true),
        DailyActivity(title = "Actividad #3", description = "Esta es la descripcion", currentProgress = 1, maxProgress = 4, isChecked = false),
        DailyActivity(title = "Actividad #4", description = "Esta es la descripcion", currentProgress = 1, maxProgress = 3, isChecked = true),
        DailyActivity(title = "Actividad #4", description = "Esta es la descripcion", currentProgress = 3, maxProgress = 3, isChecked = true),
        DailyActivity(title = "Actividad #4", description = "Esta es la descripcion", currentProgress = 3, maxProgress = 3, isChecked = true)

    )
    
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        ConstraintLayout(
        ) {
            val (text, cardColumn) = createRefs()
            Text(
                text = "Marca las actividades que realizaste hoy",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 32.sp,
                color = ColorText,
                textAlign = TextAlign.Center,
                maxLines = 2,
                modifier = Modifier
                    .constrainAs(text) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                   .padding(16.dp)
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(cardColumn) {
                        top.linkTo(text.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }, verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                listOfActivities.forEachIndexed { index, activity ->
                    item{
                        ProgressCard(
                            title = activity.title,
                            progress = activity.currentProgress,
                            maxProgress = activity.maxProgress,
                            isChecked = activity.isChecked,
                            onCheckClick = {viewModel.onActivityCheckClick(activity,index)},
                            onHelpIconClick = { showActivityDescriptionModal(activity.description) },
                        )
                    }
                }

            }

        }

    }
}

fun showActivityDescriptionModal(description: String){
    //TODO SHOW MODAL
}

@Preview(showBackground = true)
@Composable
fun PreviewActivityDay(){
    ActivityDayScreen()
}