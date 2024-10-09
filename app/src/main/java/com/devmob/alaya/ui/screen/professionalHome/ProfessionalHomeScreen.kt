package com.devmob.alaya.ui.screen.professionalHome

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.devmob.alaya.R
import com.devmob.alaya.navigation.ProfessionalNavigation.NavUtilsProfessional
import com.devmob.alaya.ui.components.CardContainer
import com.devmob.alaya.ui.components.Header
import com.devmob.alaya.ui.components.UserItem
import com.devmob.alaya.ui.theme.ColorQuaternary
import com.devmob.alaya.ui.theme.ColorText

@Composable
fun ProfessionalHomeScreen(viewModel: ProfessionalHomeViewModel, navController: NavController) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.fondo_home),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Header(viewModel.nameProfessional)
            Spacer(modifier = Modifier.width(16.dp))
            CardContainer(
                modifier = Modifier.fillMaxHeight(),
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
                                text = "Pacientes",
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
                        LazyColumn(
                            modifier = Modifier.wrapContentHeight()
                        ) {
                            items(viewModel.users) { user ->
                                UserItem(user, true) {
                                    navController.navigate(NavUtilsProfessional.Routes.PatientProfile.route)
                                }
                            }
                        }
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewProfessionalHomeScreen() {
    val navController = rememberNavController()
    ProfessionalHomeScreen(ProfessionalHomeViewModel(), navController)
}