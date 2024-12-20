package com.devmob.alaya.ui.screen.activityDayProfessional

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.devmob.alaya.ui.theme.ColorGray
import com.devmob.alaya.ui.theme.ColorPrimary
import com.devmob.alaya.ui.theme.ColorText
import com.devmob.alaya.ui.theme.LightBlueColor
import com.devmob.alaya.utils.NavUtils

@Composable
fun ActivityDayProfessionalScreen(
    viewModel: ActivityDayProfessionalViewModel,
    navController: NavController,
    email: String
){

    val uiState = viewModel.uiState.collectAsState()

    LaunchedEffect(Unit){
        viewModel.getDailyActivities(email)
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LightBlueColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(top = 32.dp)
        ) {

            when(uiState.value.isLoading){
                true -> {
                    CircularProgressIndicator(
                        color = ColorPrimary
                    )
                }
                else -> {
                    when(uiState.value.activityList.size){
                        0 -> {
                            if(uiState.value.isError){
                                Text(
                                    text = "Ocurrio un error al cargar las actividades",
                                    fontSize = 25.sp,
                                    fontWeight = FontWeight.Bold,
                                    lineHeight = 32.sp,
                                    color = ColorGray,
                                    textAlign = TextAlign.Center,
                                    maxLines = 2,
                                    modifier = Modifier.align(Alignment.CenterHorizontally)
                                )
                            }else{
                                Text(
                                    text = "Todavia no cargaste ninguna actividad",
                                    fontSize = 25.sp,
                                    fontWeight = FontWeight.Bold,
                                    lineHeight = 32.sp,
                                    color = ColorGray,
                                    textAlign = TextAlign.Center,
                                    maxLines = 2,
                                    modifier = Modifier.align(Alignment.CenterHorizontally)
                                )
                            }

                        }
                        else -> {
                            ConstraintLayout(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                val (text, card, cardColumn) = createRefs()

                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 25.dp)
                                        .clip(RoundedCornerShape(25.dp))
                                        .constrainAs(card) {
                                            top.linkTo(text.bottom, margin = 16.dp)
                                            start.linkTo(parent.start)
                                            end.linkTo(parent.end)
                                        },
                                    colors = CardDefaults.cardColors(
                                        containerColor = Color(0xFFF2EED8)
                                    )
                                ) {
                                    Text(
                                        text = "Acá vas a poder asignarle tareas semanales al paciente para que no olvide realizarlas",
                                        modifier = Modifier.padding(16.dp),
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = ColorText
                                    )
                                }

                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .constrainAs(cardColumn) {
                                            top.linkTo(card.bottom, margin = 16.dp)
                                            start.linkTo(parent.start)
                                            end.linkTo(parent.end)
                                        }
                                        .padding(horizontal = 16.dp),
                                    verticalArrangement = Arrangement.spacedBy(5.dp)
                                ) {
                                    uiState.value.activityList.forEach { item ->
                                        CardPersonalizedProfessional(
                                            title = item.title,
                                            descripcion = item.description,
                                            navController = navController,
                                            maxProgress = item.maxProgress,
                                            progress = item.currentProgress,
                                            email = email,
                                            onEditClick = {
                                                viewModel.onEditActivity(item)}
                                        )
                                    }

                                }
                            }
                        }
                    }

                }
            }


        }
        FloatingActionButton(
            onClick = {
                navController.navigate("${NavUtils.ProfessionalRoutes.ModalActivityDayProfessional.route}/$email")
                viewModel.onCreateActivity()
             },
            containerColor = ColorPrimary,
            contentColor = Color.White,
            shape = RoundedCornerShape(50.dp),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 50.dp, end = 32.dp)
                .size(80.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Agregar",
                modifier = Modifier.size(40.dp)
            )
        }


    }
}

