package com.devmob.alaya.ui.screen.ContainmentNetwork

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import com.devmob.alaya.components.Card
import com.devmob.alaya.domain.model.Contact
import com.devmob.alaya.ui.components.IconButton

@Composable
fun ContainmentNetworkScreen(
    viewModel: ContainmentNetworkViewModel,
    navController: NavController
    ) {

    val contacts by viewModel.contacts.observeAsState(initial = emptyList())

    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {
        val (contactList, iconButton) = createRefs()

        Column(
            modifier = Modifier
                .constrainAs(contactList) {
                    top.linkTo(parent.top, margin = 56.dp)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
                .padding(16.dp),

            ) {
            contacts.forEach { contact ->
                ContactCard(
                    contact = contact,
                    onClick = {
                        navController.navigate("contact_detail/${contact.contactId}")
                    }
                )
            }
        }

        IconButton(
            symbol = Icons.Outlined.Add,
            onClick = {
                navController.navigate("add_contact")
            },
            modifier = Modifier.constrainAs(iconButton) {
                bottom.linkTo(parent.bottom, margin = 0.dp)
                end.linkTo(parent.end, margin = 10.dp)
            }
        )
    }
}

@Composable
fun ContactCard(contact: Contact, onClick: () -> Unit) {
    Card(
        title = contact.name,
        subtitle = contact.numberPhone,
        imageUrl = contact.photo,
        onClick = onClick
    )
}



