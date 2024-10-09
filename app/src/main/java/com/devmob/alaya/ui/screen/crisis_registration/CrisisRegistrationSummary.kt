package com.devmob.alaya.ui.screen.crisis_registration

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.devmob.alaya.domain.model.CrisisBodySensation
import com.devmob.alaya.domain.model.CrisisEmotion
import com.devmob.alaya.domain.model.CrisisPlace
import com.devmob.alaya.domain.model.CrisisTimeDetails
import com.devmob.alaya.domain.model.CrisisTool
import com.devmob.alaya.ui.components.Button
import com.devmob.alaya.ui.components.ButtonStyle
import com.devmob.alaya.ui.theme.ColorText
import com.devmob.alaya.ui.theme.LightBlueColor

@Composable
fun CrisisRegistrationSummary(
    crisisTime: CrisisTimeDetails,
    location: CrisisPlace,
    sensations: List<CrisisBodySensation>,
    emotions: List<CrisisEmotion>,
    tools: List<CrisisTool>,
    notes: String,
    onConfirm: () -> Unit,
    onDelete: () -> Unit,
    onBackPressed:() -> Unit
    ){

    ConstraintLayout(modifier = Modifier.fillMaxSize()){

        val (informationColumn, topTitle,actionButtons) = createRefs()

        Column(
            modifier = Modifier.constrainAs(informationColumn) {
                top.linkTo(topTitle.top,margin = 8.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        ){

        }

        Row(horizontalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier.constrainAs(actionButtons){
                bottom.linkTo(parent.bottom, margin = 10.dp)
                start.linkTo(parent.start, margin = 5.dp)
                end.linkTo(parent.end,margin = 5.dp)

            }
            ){
           Button(
               text = "Eliminar",
               style = ButtonStyle.Outlined,
               onClick = onDelete
           )
            Button(
                text = "Confirmar",
                style = ButtonStyle.Filled,
                onClick = onConfirm
            )
        }
    }
}