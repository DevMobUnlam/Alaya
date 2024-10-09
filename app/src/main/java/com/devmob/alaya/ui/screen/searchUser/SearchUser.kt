package com.devmob.alaya.ui.screen.searchUser

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Icon
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.devmob.alaya.ui.components.CardContainer
import com.devmob.alaya.ui.components.UserItem
import com.devmob.alaya.ui.theme.ColorWhite
import com.devmob.alaya.ui.theme.LightBlueColor
import com.devmob.alaya.utils.NavUtils

@Composable
fun SearchUserScreen(viewModel: SearchUserViewModel, navController: NavController) {
    var searchText by remember { mutableStateOf(TextFieldValue("")) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LightBlueColor)
    ) {
        BasicTextField(
            value = searchText,
            onValueChange = { searchText = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            singleLine = true,
            textStyle = TextStyle(fontSize = TextUnit(16f,TextUnitType.Sp)),
            decorationBox = { innerTextField ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(ColorWhite, shape = RoundedCornerShape(25.dp))
                        .padding(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Icono de búsqueda",
                        tint = Color.Gray,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(Modifier.weight(1f)) {
                        if (searchText.text.isEmpty()) {
                            Text("Buscar pacientes", color = Color.LightGray)
                        }
                        innerTextField()
                    }
                }
            }
        )
        
        CardContainer(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth(),
            enabled = true,
            content = {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(viewModel.getUsersFilter(searchText.text)) { user ->
                        UserItem(
                            user,
                            false
                        ) {
                            navController.navigate(NavUtils.ProfessionalRoutes.PatientProfile.route)
                        }
                    }
                }
            }
        )
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewSearchUser() {
    val navController = rememberNavController()
    val viewModel = SearchUserViewModel()
    SearchUserScreen(viewModel, navController)
}
