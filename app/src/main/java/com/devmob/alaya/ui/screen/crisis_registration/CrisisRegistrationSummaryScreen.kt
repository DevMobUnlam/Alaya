package com.devmob.alaya.ui.screen.crisis_registration

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CrisisAlert
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.devmob.alaya.ui.components.Button
import com.devmob.alaya.ui.components.ButtonStyle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.devmob.alaya.R
import com.devmob.alaya.domain.model.Intensity
import com.devmob.alaya.ui.components.Modal
import com.devmob.alaya.ui.theme.ColorGray
import com.devmob.alaya.ui.theme.ColorText
import com.devmob.alaya.ui.theme.ColorWhite
import com.devmob.alaya.ui.theme.LightBlueColor
import com.devmob.alaya.utils.NavUtils
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


/**
 * Pantalla de Resumen de Registro de Crisis
 *
 * Se muestran los datos del formulario de registro
 *
 * onConfirm: Accion a ejecutar una vez confirmado el registro de crisis
 *
 * onDelete: Accion a ejecutar una vez se aprete en eliminar datos del formulario
 *
 * onEditClick: Accion a ejecutar una vez se aprete en el icono para editar el respectivo paso del formulario. Recibe un Int que seria el paso a editar en cuestion
 *
 */

@Composable
fun CrisisRegistrationSummaryScreen(
    modifier: Modifier = Modifier,
    viewModel: CrisisRegistrationViewModel = viewModel(),
    onConfirm: () -> Unit = {},
    onDelete: () -> Unit = {},
    onEditClick: (Int) -> Unit = {},
    navController: NavController
    ){

    val screenState = viewModel.screenState.observeAsState()
    //val shouldShowExitModal = viewModel.shouldShowModal
    var showModalDelete by remember { mutableStateOf(false) }
    var showModalConfirm by remember { mutableStateOf(false) }


    ConstraintLayout(modifier = modifier.fillMaxSize()){

        val (informationColumn, actionButtons) = createRefs()

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            // Contenedor que permite hacer scroll si hay muchas tarjetas.
            LazyColumn(
                modifier = Modifier
                    .weight(1f) // Asegura que la lista ocupe todo el espacio disponible.
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
            item{
                screenState.value?.crisisDetails?.crisisTimeDetails?.let { date ->
                    SummaryItemCard(
                        title = "Inicio y Fin",
                        startContent = {
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)){
                                //TODO()  Agregar el atributo de fechas y horas del viewModel, con su respectivo formatter
                                Text(text = dateFormatter(date.startingDate), fontSize = 19.sp,  color = ColorText)
                                Text(text = dateFormatter(date.endDate), fontSize = 19.sp,  color = ColorText)
                            }
                        },
                        endContent = {
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)){
                                Text(text = timeFormatter(date.startTIme), fontSize = 19.sp, color = ColorText)
                                Text(text = timeFormatter(date.endTime), fontSize = 19.sp, color = ColorText)
                            }
                        },
                        onEditClick = onEditClick,
                        step = 1
                    )
                }
            }

            item{
                SummaryItemCard(
                    title = stringResource(R.string.place),
                    startContent = {
                        Column{
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically){
                                Icon(Icons.Outlined.Home, contentDescription = stringResource(R.string.place),
                                    tint = ColorText, modifier = Modifier.size(35.dp))
                                Text(text = "Casa", fontSize = 21.sp,  color = ColorText, fontWeight = FontWeight.Bold)
                            }
                        }
                    },
                    onEditClick = onEditClick,
                    step = 2
                )
            }

            item{
                SummaryItemCard(
                    title = stringResource(R.string.body_sensations),
                    startContent = {
                        Column{
                            // TODO() Remplazar con items() para que recorra la lista de sensaciones por atributo de viewModel
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Icon(
                                        Icons.Outlined.Refresh,
                                        contentDescription = stringResource(R.string.place),
                                        tint = ColorText,
                                        modifier = Modifier.size(35.dp),
                                    )
                                    Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
                                        Text(
                                            text = "Mareo",
                                            fontSize = 21.sp,
                                            color = ColorText,
                                            fontWeight = FontWeight.Bold,
                                        )
                                        Text(
                                            // TODO() PASAR INTENSIDAD RECIBIDA DE OBJETO, Y USAR FORMATTER
                                            text = "Intensidad: Media",
                                            fontSize = 19.sp,
                                            color = ColorText,
                                        )
                                }
                            }
                        }
                    },
                    onEditClick = onEditClick,
                    step = 3
                )
            }

            item{
                SummaryItemCard(
                    title = stringResource(R.string.emotions),
                    startContent = {
                        Column{
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically){
                                Icon(Icons.Outlined.CrisisAlert, contentDescription = stringResource(R.string.place),
                                    tint = ColorText, modifier = Modifier.size(35.dp))
                                Column(verticalArrangement = Arrangement.spacedBy(1.dp)){
                                    Text(text = "Miedo", fontSize = 21.sp,  color = ColorText, fontWeight = FontWeight.Bold)
                                    Text(text = "Intensidad: Alta", fontSize = 21.sp,  color = ColorText)

                                }
                            }
                        }
                    },
                    onEditClick = onEditClick,
                    step = 4
                )
            }

            item{
                SummaryItemCard(
                    title = stringResource(R.string.tools),
                    startContent = {
                        Column{
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically){
                                Icon(Icons.Outlined.CrisisAlert, contentDescription = stringResource(R.string.place),
                                    tint = ColorText, modifier = Modifier.size(35.dp))
                                Column(verticalArrangement = Arrangement.spacedBy(1.dp)){
                                    Text(text = "Imaginación", fontSize = 21.sp,  color = ColorText, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    },
                    onEditClick = onEditClick,
                    step = 5
                )
            }

            item{
                SummaryItemCard(
                    title = stringResource(R.string.notes),
                    startContent = {
                        Text(
                            // TODO() Agregar atributo del viewModel para cargar las notas
                            "Este es el texto que tiene que ir en el caso de las notas que registro el usuario, vemos como se corta en cierta parte para que tenga que editarlo si quiere verlo todo. Se corta de por si ya que tiene un maximo de 3 lineas" ,
                            fontSize = 19.sp,
                            color = ColorText,
                            maxLines = 3
                        )
                    },
                    onEditClick = onEditClick,
                    step = 6
                )
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(5.dp),
           modifier = Modifier.align(Alignment.CenterHorizontally)
            ){
           Button(
               text = "Eliminar",
               style = ButtonStyle.Outlined,
               onClick = { showModalDelete = true }
           )
            Button(
                text = "Confirmar",
                style = ButtonStyle.Filled,
                onClick = { showModalConfirm = true }
            )
        }
            Modal(
                show = showModalDelete,
                title = "¿Seguro que quiere eliminar el registro?",
                primaryButtonText = "Si",
                secondaryButtonText = "No" ,
                onDismiss = { showModalDelete = false },
                onConfirm = {
                    showModalDelete = false
                    navController.navigate(NavUtils.PatientRoutes.Home.route)
                }
            )

            Modal(
                show = showModalConfirm,
                title = "¿Quiere confirmar el registro?",
                primaryButtonText = "Si",
                secondaryButtonText = "No" ,
                onDismiss = { showModalConfirm = false },
                onConfirm = {
                    showModalConfirm = false
                    navController.navigate(NavUtils.PatientRoutes.Home.route)
                }
            )


        }
    }
}


@Composable
fun SummaryItemCard(
    modifier: Modifier = Modifier,
    title: String,
    step: Int = 1,
    onEditClick: (Int) -> Unit,
    startContent: @Composable () -> Unit,
    endContent: @Composable (() -> Unit)? = null
){
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = ColorWhite),
        elevation = CardDefaults.cardElevation(4.dp)
    ){
        Column(
            modifier = Modifier.padding(9.dp),
            verticalArrangement = Arrangement.spacedBy(1.dp)
        ){
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(text = title, fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = ColorText)
                Icon(modifier = Modifier
                    .size(20.dp)
                    .clickable {
                        onEditClick(step)
                    },imageVector = Icons.Outlined.Edit, contentDescription = "Editar", tint = ColorGray)

            }
            Spacer(modifier = Modifier.height(3.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){

                startContent()
                endContent?.invoke()
            }
        }


    }
}

/**
 * Formateador de Intensidad que devuelve como un String el tipo de intensidad que reciba por parametro
 */
fun IntensityFormatter(intensity: Intensity): String{
    return when(intensity){
        Intensity.LOW -> R.string.intensity_low.toString()
        Intensity.MEDIUM -> R.string.intensity_medium.toString()
        Intensity.HIGH -> R.string.intensity_high.toString()
    }
}

private fun dateFormatter(date: Date): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return formatter.format(date)
}

private fun timeFormatter(date: Date): String {
    val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
    return formatter.format(date)
}
