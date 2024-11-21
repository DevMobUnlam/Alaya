package com.devmob.alaya.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devmob.alaya.ui.screen.patient_profile.CarouselItem
import com.devmob.alaya.ui.theme.ColorQuaternary
import com.devmob.alaya.ui.theme.ColorTertiary
import com.devmob.alaya.ui.theme.ColorText

@Composable
fun HorizontalCardCarousel(modifier: Modifier, items: List<CarouselItem>, onGenerateIASummary: () -> Unit) {

    LazyRow(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items) { item ->
            CarouselCard(item, onGenerateIASummary)
        }
    }
}

@Composable
fun CarouselCard(item: CarouselItem, onGenerateIASummary :() -> Unit) {
    Card(
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 9.dp),
        modifier = Modifier
            .width(150.dp)
            .height(150.dp)
            .clickable{if(item is CarouselItem.GenerateSummary){
                onGenerateIASummary()
            }
            }
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
                            fontSize = 32.sp,
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
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        TitleText(item.title)
                        Spacer(modifier = Modifier.height(4.dp))
                        item.tools.forEach { tool ->
                            Text(
                                text = tool.name,
                                fontWeight = FontWeight.Normal,
                                color = ColorText
                            )
                            LinearProgressIndicator(
                                progress = tool.progress,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(10.dp)
                                    .padding(vertical = 2.dp),
                                color = ColorTertiary,
                                trackColor = ColorQuaternary,
                                strokeCap = StrokeCap.Round
                            )
                        }
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
        color = ColorText,
        fontWeight = FontWeight.SemiBold
    )
}

@Preview(showBackground = true)
@Composable
fun HorizontalCardCarouselPreview() {
    HorizontalCardCarousel(modifier = Modifier.fillMaxWidth(), items = listOf(), onGenerateIASummary = {})
}
