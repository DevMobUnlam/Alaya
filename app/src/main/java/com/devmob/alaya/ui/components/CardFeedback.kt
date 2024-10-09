package com.devmob.alaya.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devmob.alaya.R
import com.devmob.alaya.ui.theme.ColorText

@Composable
fun CardFeedback(
    textID: Int,
    imageID: Int
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth(),
//            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(8.dp),
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 10.dp, horizontal = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = stringResource(id = textID),
                fontWeight = FontWeight.W400,
                fontSize = 25.sp,
                color = ColorText,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .align(Alignment.CenterHorizontally),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(20.dp))
            Image(
                painter = painterResource(id = imageID),
                contentDescription = "Imagen",
                modifier = Modifier
                    .height(300.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Fit
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCardFeedbackFelicitaciones() {
    CardFeedback(
        textID = R.string.feedback_felicitaciones,
        imageID = R.drawable.feedback_felicitaciones
    )
}
@Preview(showBackground = true)
@Composable
fun PreviewCardFeedbackTodoVaEstarBien() {
    CardFeedback(
        textID = R.string.feedback_va_aestarbien,
        imageID = R.drawable.feedback_todovaaestarbien
    )
}