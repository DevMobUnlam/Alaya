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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.rememberImagePainter
import com.devmob.alaya.R
import com.devmob.alaya.ui.components.Button
import com.devmob.alaya.ui.components.ButtonStyle
import androidx.compose.ui.text.input.KeyboardType
import com.devmob.alaya.ui.screen.ContainmentNetwork.ContainmentNetworkViewModel

@Composable
fun ContactScreen(
    contactId: String,
    viewModel: ContainmentNetworkViewModel,
    navController: NavController
) {
    val contacts by viewModel.contacts.observeAsState(emptyList())
    val contact = contacts.find { it.contactId == contactId }

    var showDeleteModal by remember { mutableStateOf(false) }
    var showEditModal by remember { mutableStateOf(false) }

    contact?.let { currentContact ->
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
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

            DetailCardContact(
                contact = currentContact,
                modifier = Modifier.constrainAs(contactCard) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }.padding(16.dp),
            )

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
                    onClick = { showEditModal = true},
                    text= "Editar",
                    style = ButtonStyle.Filled,
                )

                Button(
                    onClick = { showDeleteModal = true },
                    text= "Eliminar",
                    style = ButtonStyle.Outlined
                )
        }

        Modal(
            show = showDeleteModal,
            title = "¿Estás seguro que deseas eliminar este contacto?",
            primaryButtonText = "Sí",
            secondaryButtonText = "No",
            onConfirm = {
                viewModel.deleteContact(currentContact)
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
                    viewModel.editContact(updatedContact)
                    showEditModal = false
                }
            )
        }
    }

}}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditContactModal(
    contact: Contact,
    onDismiss: () -> Unit,
    onSave: (Contact) -> Unit
) {
    var name by remember { mutableStateOf(contact.name) }
    var phone by remember { mutableStateOf(contact.numberPhone) }
    var photoUri by remember { mutableStateOf<Uri?>(null) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        photoUri = uri
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar Contacto") },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                photoUri?.let {
                    Image(
                        painter = rememberImagePainter(it),
                        contentDescription = "Imagen de contacto",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .clickable { galleryLauncher.launch("image/*") },
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                Input(value = name, onValueChange = { name = it }, label = "Nombre", placeholder = "Ingresa el nombre")
                Spacer(modifier = Modifier.height(8.dp))
                Input(value = phone, onValueChange = { phone = it }, label = "Teléfono", placeholder = "Ingresa el teléfono", keyboardType = KeyboardType.Phone)
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onSave(Contact(contact.contactId, name, phone, photoUri?.toString() ?: contact.photo))
                }
            ) { Text("Guardar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

