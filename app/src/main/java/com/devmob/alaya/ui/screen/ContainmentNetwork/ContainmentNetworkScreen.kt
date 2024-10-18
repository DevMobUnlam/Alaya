package com.devmob.alaya.ui.screen.ContainmentNetwork

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.devmob.alaya.R
import com.devmob.alaya.ui.components.IconButton
import com.devmob.alaya.ui.screen.ContainmentNetwork.Contact.ContactCard
import com.devmob.alaya.ui.screen.ContainmentNetwork.Contact.ContactViewModel

@Composable
fun ContainmentNetworkScreen(
    viewModel: ContainmentNetworkViewModel,
    navController: NavController
    ) {
    val contacts by viewModel.contacts.observeAsState(initial = emptyList())
    val context = LocalContext.current

    val contactPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickContact()
    ) { contactUri: Uri? ->
        contactUri?.let {
            viewModel.addContactFromPhone(context, it)
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            contactPickerLauncher.launch(null)
        }
    }

    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {
        val (backgroundImage, contactList, iconButton) = createRefs()
        Image(
            painter = painterResource(id = R.drawable.fondo_home),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .constrainAs(backgroundImage) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            contentScale = ContentScale.Crop
        )
        LazyColumn(
            modifier = Modifier
                .constrainAs(contactList) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
                .padding(16.dp),

            ) {
            items(contacts) { contact ->
                ContactCard(
                    contact = contact,
                    viewModel = ContactViewModel(),
                    onClick = {
                        navController.navigate("contact_detail/${contact.contactId}")
                    }
                )
            }
        }

        /*IconButton(
            symbol = Icons.Outlined.Add,
            onClick = {
                when {
                    ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED -> {
                        contactPickerLauncher.launch(null)
                    }
                    else -> {
                        permissionLauncher.launch(Manifest.permission.READ_CONTACTS)
                    }
                }
            },
            modifier = Modifier.constrainAs(iconButton) {
                bottom.linkTo(parent.bottom, margin = 0.dp)
                end.linkTo(parent.end, margin = 10.dp)
            }
        )*/
    }
}







