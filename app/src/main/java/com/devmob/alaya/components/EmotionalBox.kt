package com.devmob.alaya.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.devmob.alaya.ui.theme.ColorWhite
import com.devmob.alaya.R
import com.devmob.alaya.ui.theme.ColorPrimary

@Composable
fun EmotionalBox(
    textID: Int
){
    Box(
        modifier= Modifier
            .width(110.dp)
            .height(70.dp)
            .background(color = ColorPrimary, shape = RoundedCornerShape(10.dp))
        ,
        contentAlignment = Alignment.Center
    ){
      Text (
          text = stringResource(id = textID),
          fontWeight = FontWeight.W400,
          color = ColorWhite,
          textAlign = TextAlign.Center,
          fontSize = 16.sp
      )

    }
}
@Composable
fun EmotionalGrid() {
    val emotionalTexts = listOf(
        R.string.feliz,
        R.string.triste,
        R.string.enojado,
        R.string.calmo,
        R.string.ansioso,
        R.string.confiado,
        R.string.inseguro,
        R.string.indiferente,
        R.string.angustiado
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        content = {
            items(emotionalTexts) { textID ->
                EmotionalBox(textID = textID)
            }
        },
        contentPadding = PaddingValues(20.dp),
        modifier = Modifier.padding(30.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewEmotionalBox() {
    EmotionalGrid()
}
