package com.devmob.alaya.ui.screen.ContainmentNetwork

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.devmob.alaya.domain.model.Contact
import com.devmob.alaya.ui.components.ContactCard
import com.devmob.alaya.ui.components.Modal
import com.devmob.alaya.ui.theme.ColorText

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
            val (contactCard, actionRow) = createRefs()

            ContactCard(
                name = currentContact.name,
                number = currentContact.numberPhone,
                imageUrl = currentContact.photo,
                modifier = Modifier.constrainAs(contactCard) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )

            Row(
                modifier = Modifier
                    .constrainAs(actionRow) {
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = { showEditModal = true }) {
                    Text("Editar")
                }

                Button(
                    onClick = { showDeleteModal = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red
                    )
                ) {
                    Text("Eliminar")
                }
            }
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

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditContactModal(
    contact: Contact,
    onDismiss: () -> Unit,
    onSave: (Contact) -> Unit
) {
    var name by remember { mutableStateOf(contact.name) }
    var phone by remember { mutableStateOf(contact.numberPhone) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Editar Contacto",
                    color = ColorText,
                    fontWeight = FontWeight.Medium,
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center
                )
            }
        },
        text = {
            Column {
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre") },
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Blue,
                        unfocusedIndicatorColor = Color.Gray
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Número de Teléfono") },
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Blue,
                        unfocusedIndicatorColor = Color.Gray
                    )
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onSave(Contact(contact.contactId, name, phone, contact.photo))
                }
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}