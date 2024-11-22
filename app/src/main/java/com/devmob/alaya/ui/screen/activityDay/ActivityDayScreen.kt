package com.devmob.alaya.ui.screen.activityDay

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.devmob.alaya.ui.components.DailyActivityDescriptionModal
import com.devmob.alaya.ui.theme.ColorGray
import com.devmob.alaya.ui.theme.ColorText

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
    ) {
        ConstraintLayout(
            modifier = Modifier
        ) {
            val (text, cardColumn, descriptionModal, postActivityModal, loadingIndicator) = createRefs()
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
                            text = "Todavia no tenes ninguna actividad cargada para hacer",
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
                                }
                                .verticalScroll(rememberScrollState())
                                .padding(bottom = 120.dp),
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

                        this@Column.AnimatedVisibility(
                            visible = viewModel.shouldShowDescriptionModal,
                            modifier = Modifier.constrainAs(descriptionModal){
                                top.linkTo(text.bottom)
                                start.linkTo(parent.start, margin = 10.dp)
                                end.linkTo(parent.end, margin = 10.dp)
                                bottom.linkTo(parent.bottom)
                            }.shadow(elevation = 2.dp)
                        ){
                            viewModel.focusedActivity.let {
                                DailyActivityDescriptionModal(
                                    title = it.title,
                                    description = it.description,
                                    onDismiss = { viewModel.hideActivityDescriptionModal() },
                                )
                            }
                        }
                        this@Column.AnimatedVisibility(
                            visible = viewModel.shouldShowPostActivityModal,
                            modifier = Modifier.constrainAs(postActivityModal){
                                top.linkTo(parent.top)
                                start.linkTo(parent.start, margin = 10.dp)
                                end.linkTo(parent.end, margin = 10.dp)
                                bottom.linkTo(parent.bottom)
                            }
                                .shadow(
                                    elevation = 8.dp,
                                    shape = RoundedCornerShape(8.dp)
                                )
                        ){
                            ActivityDayCard(
                                onSave = { viewModel.onSavePostActivityComment(it) },
                                onSkip = {viewModel.hidePostActivityModal()}
                            )
                        }
                    }

                }
            }



        }

    }
}


@Preview(showBackground = true)
@Composable
fun PreviewActivityDay(){
    ActivityDayScreen()
}