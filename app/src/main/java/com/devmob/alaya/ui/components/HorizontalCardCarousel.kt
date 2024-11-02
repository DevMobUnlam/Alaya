package com.devmob.alaya.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devmob.alaya.ui.theme.ColorPrimary
import com.devmob.alaya.ui.theme.ColorQuaternary
import com.devmob.alaya.ui.theme.ColorTertiary
import com.devmob.alaya.ui.theme.ColorText
import com.devmob.alaya.ui.theme.LightBlueColor

@Composable
fun HorizontalCardCarousel(modifier: Modifier) {

    val items = listOf(
        CarouselItem.GenerateSummary(ColorQuaternary),
        CarouselItem.Crisis("Crisis", "5", ColorTertiary.copy(alpha = 0.2f)),
        CarouselItem.Activities("Actividades", 0.7f, LightBlueColor),
        CarouselItem.Tools("Herramientas", ColorPrimary.copy(alpha = 0.2f))
    )

    LazyRow(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items) { item ->
            CarouselCard(item)
        }
    }
}

@Composable
fun CarouselCard(item: CarouselItem) {
    Card(
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 9.dp),
        modifier = Modifier
            .width(150.dp)
            .height(100.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(item.colorBackground),
            contentAlignment = Alignment.Center
        ) {
            when (item) {
                is CarouselItem.GenerateSummary -> {
                    TitleText(item.title)
                }

                is CarouselItem.Crisis -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        TitleText(item.title)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = item.count,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            textAlign = TextAlign.Center,
                            color = ColorText
                        )
                    }
                }

                is CarouselItem.Activities -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        TitleText(item.title)
                        Spacer(modifier = Modifier.height(4.dp))
                        CircleProgress(progress = item.progress)
                    }
                }

                is CarouselItem.Tools -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        TitleText(item.title)
                        Spacer(modifier = Modifier.height(4.dp))
                        //Grafico de barras
                    }
                }
            }
        }
    }
}

@Composable
fun TitleText(title: String) {
    Text(
        text = title,
        fontSize = 20.sp,
        textAlign = TextAlign.Center,
        color = ColorText
    )
}

@Preview(showBackground = true)
@Composable
fun HorizontalCardCarouselPreview() {
    HorizontalCardCarousel(modifier = Modifier.fillMaxWidth())
}

sealed class CarouselItem(val title: String, val colorBackground: Color) {
    class GenerateSummary(colorBackground: Color) :
        CarouselItem("Generar\nResumen", colorBackground)

    class Crisis(title: String, val count: String, colorBackground: Color) :
        CarouselItem(title, colorBackground)

    class Activities(title: String, val progress: Float, colorBackground: Color) :
        CarouselItem(title, colorBackground)

    class Tools(title: String, colorBackground: Color) : CarouselItem(title, colorBackground)
}