package com.devmob.alaya.ui.screen.activityDay

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.devmob.alaya.ui.theme.ColorText

@Composable
fun ActivityDayScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        ConstraintLayout(
            modifier = Modifier
        ) {
            val (text, cardColumn) = createRefs()
            Text(
                text = "Marca las actividades que realizaste hoy ",
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
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(cardColumn) {
                        top.linkTo(text.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .padding(16.dp)
            ) {
              ProgressCard(title = "Meditacion guiada", progress = 3, maxProgress =7 )
                ProgressCard(title = "Exposición gradual", progress = 1, maxProgress =2 )
                ProgressCard(title = "Ir a visitar a un amigo/a", progress = 1, maxProgress =1)


            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun PreviewActivityDay(){
    ActivityDayScreen()
}