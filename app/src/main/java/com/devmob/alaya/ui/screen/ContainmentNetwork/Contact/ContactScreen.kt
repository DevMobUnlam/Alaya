package com.devmob.alaya.ui.screen.ContainmentNetwork.Contact

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.devmob.alaya.domain.model.Contact
import com.devmob.alaya.ui.components.Input
import com.devmob.alaya.ui.components.Modal
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import coil.compose.rememberImagePainter
import com.devmob.alaya.R
import com.devmob.alaya.ui.components.Button
import com.devmob.alaya.ui.components.ButtonStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.rememberAsyncImagePainter
import com.devmob.alaya.components.getInitials
import com.devmob.alaya.data.FirebaseClient
import com.devmob.alaya.ui.screen.ContainmentNetwork.ContainmentNetworkViewModel
import com.devmob.alaya.ui.theme.ColorPrimary
import com.devmob.alaya.ui.theme.ColorWhite
import com.devmob.alaya.ui.theme.LightBlueColor

@Composable
fun ContactScreen(
    contactId: String,
    viewModel: ContainmentNetworkViewModel,
    navController: NavController
) {
    val contacts by viewModel.contacts.observeAsState(emptyList())
    val contact = contacts.find { it.contactId == contactId }
    val email = FirebaseClient().auth.currentUser?.email

    var showDeleteModal by remember { mutableStateOf(false) }
    var showEditModal by remember { mutableStateOf(false) }

    LaunchedEffect(contactId) {
        viewModel.listenToContacts()
    }

    contact?.let { currentContact ->
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
        ) {
            val (backgroundImage, contactCard, actionRow) = createRefs()
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

            Box(
                modifier = Modifier
                    .constrainAs(contactCard) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .padding(16.dp)
            ) {
                DetailCardContact(
                    contact = currentContact,
                    modifier = Modifier.fillMaxWidth()
                )
            }


        if(currentContact.contactId != "4") {
            Row(
                modifier = Modifier
                    .constrainAs(actionRow) {
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { showEditModal = true },
                    text = "Editar",
                    style = ButtonStyle.Filled,
                )

                Button(
                    onClick = { showDeleteModal = true },
                    text = "Eliminar",
                    style = ButtonStyle.Outlined
                )
            }
        }

        Modal(
            show = showDeleteModal,
            title = "¿Estás seguro que deseas eliminar este contacto?",
            primaryButtonText = "Sí",
            secondaryButtonText = "No",
            onConfirm = {
                if (email != null) {
                    viewModel.deleteContact(email, currentContact)
                }
                showDeleteModal = false
                navController.popBackStack()
            },
            onDismiss = {
                showDeleteModal = false
            }
        )

        if (showEditModal) {
            EditContactModal(
                contact = currentContact,
                onDismiss = { showEditModal = false },
                onSave = { updatedContact ->
                    if (email != null) {
                        viewModel.editContact(email, updatedContact)
                    }
                    showEditModal = false
                }
            )
        }
    }

}}


@Composable
fun EditContactModal(
    contact: Contact,
    onDismiss: () -> Unit,
    onSave: (Contact) -> Unit
) {
    var name by rememberSaveable { mutableStateOf(contact.name) }
    var phone by remember { mutableStateOf(contact.numberPhone) }
    var photoUri by rememberSaveable {
        mutableStateOf(contact.photo?.let { Uri.parse(it) })
    }
    val initials = remember { getInitials(name) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            photoUri = uri
        }
    }

    LaunchedEffect(contact.photo) {
        if (photoUri == null && !contact.photo.isNullOrBlank()) {
            photoUri = Uri.parse(contact.photo)
        }
    }

    val isModified = remember(name, phone, photoUri) {
        name != contact.name ||
                phone != contact.numberPhone ||
                photoUri?.toString() != contact.photo
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar Contacto") },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(ColorPrimary)
                        .clickable { galleryLauncher.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    if (photoUri == null) {
                        val initials = initials
                        Text(
                            text = initials,
                            color = ColorWhite,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    } else {
                        Image(
                            painter = rememberAsyncImagePainter(model = photoUri),
                            contentDescription = "Imagen de contacto",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }

                    IconButton(
                        onClick = { galleryLauncher.launch("image/*") },
                        modifier = Modifier
                            .offset(
                                x = 30.dp,
                                y = 20.dp
                            )
                            .size(36.dp)
                            .background(LightBlueColor.copy(0.5f), CircleShape)
                            .zIndex(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Edit,
                            contentDescription = "Editar imagen",
                            tint = ColorPrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Input(
                    value = name,
                    onValueChange = { name = it },
                    label = "Nombre",
                    placeholder = "Ingresa el nombre"
                )
                Spacer(modifier = Modifier.height(8.dp))

                Input(
                    value = phone,
                    onValueChange = { phone = it },
                    label = "Teléfono",
                    placeholder = "Ingresa el teléfono",
                    keyboardType = KeyboardType.Phone
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onSave(
                        contact.copy(
                            name = name,
                            numberPhone = phone,
                            photo = photoUri?.toString()
                        )
                    )
                },
                enabled = isModified
            ) { Text("Guardar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}


