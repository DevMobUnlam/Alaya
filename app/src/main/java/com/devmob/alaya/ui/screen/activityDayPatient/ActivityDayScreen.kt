package com.devmob.alaya.ui.screen.activityDayPatient

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.devmob.alaya.ui.components.Button
import com.devmob.alaya.ui.components.ButtonStyle
import com.devmob.alaya.ui.theme.ColorGray
import com.devmob.alaya.ui.theme.ColorText
import com.devmob.alaya.ui.theme.LightBlueColor

@Composable
fun ActivityDayScreen(
    viewModel: ActivityDayViewModel = hiltViewModel()) {

    val uiState = viewModel.uiState.collectAsStateWithLifecycle()



    LaunchedEffect(Unit){
            viewModel.loadDailyActivities()
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        ConstraintLayout(
            modifier = Modifier
        ) {
            val (text, cardColumn,loadingIndicator) = createRefs()
            when(uiState.value.isLoading){
                true -> CircularProgressIndicator(modifier = Modifier.constrainAs(loadingIndicator){
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                })
                false -> {
                    if(uiState.value.activityList.isEmpty()){
                        Text(
                            text = "Todavía no tenés ninguna actividad cargada para hacer",
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 32.sp,
                            color = ColorGray,
                            textAlign = TextAlign.Center,
                            maxLines = 2,
                            modifier = Modifier
                                .constrainAs(text) {
                                    top.linkTo(parent.top)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                    bottom.linkTo(parent.bottom)
                                }
                                .padding(16.dp)
                        )
                    }else{
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

                        Column(
                            modifier = Modifier.fillMaxWidth()
                                .constrainAs(cardColumn){
                                    top.linkTo(text.bottom)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                },
                            verticalArrangement = Arrangement.spacedBy(5.dp)
                        ){
                            uiState.value.activityList.forEach{ activity ->
                                ProgressCard(
                                    title = activity.title,
                                    progress = activity.currentProgress,
                                    maxProgress = activity.maxProgress,
                                    isChecked = activity.isDone,
                                    onCheckClick = { viewModel.onActivityCheckClick(activity) },
                                    onHelpIconClick = { viewModel.showActivityDescriptionModal(activity) },
                                )

                            }

                        }

                        DescriptionDialog(
                            show = viewModel.shouldShowDescriptionModal,
                            description = viewModel.focusedActivity.description,
                            title = viewModel.focusedActivity.title,
                            onConfirm = {viewModel.hideActivityDescriptionModal()}
                        )
                        

                        PostActivityModal(
                            show = viewModel.shouldShowPostActivityModal,
                            onSave = { viewModel.onSavePostActivityComment(it) },
                            onSkip = { viewModel.hidePostActivityModal() },
                        )

                    }

                }
            }



        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostActivityModal(show: Boolean, onSave: (String) -> Unit = {}, onSkip: () -> Unit = {}){
    if(!show) return
    BasicAlertDialog(
        onDismissRequest = {}
    ) {
       ActivityDayCard(
       onSave = {onSave(it)},
       onSkip = {onSkip()})
    }
    
}

@Composable
fun DescriptionDialog(show: Boolean, title:String, description: String, onConfirm :() -> Unit){
        if (!show) return
        AlertDialog(
            onDismissRequest = {},
            confirmButton = {
                Button(
                    "Entiendo",
                    Modifier.fillMaxWidth(),
                    ButtonStyle.Filled,
                    onConfirm
                )
            },
            containerColor = LightBlueColor,
            title = {
                Row{
                    Text(
                        text = title,
                        color = ColorText,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.weight(1f),
                        fontSize = 24.sp,
                        textAlign = TextAlign.Center
                    )
                }

            },
            text = {
                Text(
                    text = description,
                    color = ColorText,
                    fontWeight = FontWeight.Normal,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center
                )
            }
        )

}

@Preview(showBackground = true)
@Composable
fun PreviewActivityDay(){
    ActivityDayScreen()
}