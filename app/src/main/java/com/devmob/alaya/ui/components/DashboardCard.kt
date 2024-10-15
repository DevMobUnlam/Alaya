package com.devmob.alaya.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devmob.alaya.R
import com.devmob.alaya.ui.theme.ColorQuaternary
import com.devmob.alaya.ui.theme.ColorText
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.devmob.alaya.ui.theme.ColorGray


@Composable
fun DashboardCard(modifier : Modifier = Modifier) {
    CardContainer(
        modifier = modifier,
        enabled = true,
        content = {
            Column(Modifier.padding(13.dp, 0.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                )
                {
                    Text(
                        text = "Dashboard",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = ColorText
                    )

                    Icon(
                        imageVector = Icons.Filled.ArrowForwardIos,
                        contentDescription = "Right Icon",
                        tint = ColorText,
                        modifier = Modifier
                            .size(24.dp)
                            .align(Alignment.CenterVertically)
                    )
                }
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    color = ColorQuaternary,
                    thickness = 1.dp
                )
                Column(modifier= Modifier.padding(16.dp)) {
                    Row(
                        modifier= Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(R.drawable.happy_face),
                            contentDescription = "Emoji",
                            modifier = Modifier.size(48.dp)
                        )
                        Text(
                            text = "60% del tiempo tuvo buen ánimo",
                            fontWeight = FontWeight.W400,
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center,
                            color= ColorText
                        )

                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        color = ColorGray,
                        thickness = 1.dp
                    )
                    Spacer(modifier = Modifier.height(8.dp))


                    Row(
                        Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(R.drawable.poker_face),
                            contentDescription = "Emoji",
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "2 días realizó las actividades diarias",
                            fontWeight = FontWeight.W400,
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center,
                            color = ColorText
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        color = ColorGray,
                        thickness = 1.dp
                    )
                    Spacer(modifier = Modifier.height(8.dp))


                    Row(
                        Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(R.drawable.happy_face),
                            contentDescription = "Emoji",
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Registró 8 entradas en su diario",
                            fontWeight = FontWeight.W400,
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center,
                            color = ColorText
                        )
                    }
                }
            }
        }
    )
}