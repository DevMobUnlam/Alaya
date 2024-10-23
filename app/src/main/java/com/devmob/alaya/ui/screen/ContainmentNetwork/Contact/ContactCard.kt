package com.devmob.alaya.ui.screen.ContainmentNetwork.Contact

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.devmob.alaya.R
import com.devmob.alaya.components.getInitials
import com.devmob.alaya.domain.model.Contact
import com.devmob.alaya.ui.theme.ColorPrimary
import com.devmob.alaya.ui.theme.ColorText
import com.devmob.alaya.ui.theme.ColorWhite

@Composable
fun ContactCard(
    contact: Contact,
    viewModel: ContactViewModel,
    onClick: () -> Unit,
    backgroundColor: Color = Color.White,
    textColor: Color = ColorText,
    showWhatsappButton: Boolean = true
){
    val context = LocalContext.current
    ElevatedCard(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .wrapContentHeight(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor,
        ),
        elevation = CardDefaults.cardElevation(8.dp),

        ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween

        ) {
                if (contact.photo != null){
                    Image(
                        painter = rememberAsyncImagePainter(
                            model = contact.photo,
                            contentScale = ContentScale.Crop,
                        ),
                        contentScale = ContentScale.Crop,
                        contentDescription = "Imagen de tarjeta",
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                } else {
                    val initials = getInitials(contact.name)
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(ColorPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = initials,
                            color = ColorWhite,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                }

            Column(modifier = Modifier.weight(1f)) {
                Text(text = contact.name, fontWeight = FontWeight.Bold, fontSize = 18.sp, color= textColor)
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(text = contact.numberPhone, fontSize = 14.sp, color = textColor)
            }

                Spacer(modifier = Modifier.width(16.dp))

            Column(){
                if(showWhatsappButton) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF25D366)),
                        contentAlignment = Alignment.Center
                    ) {
                        IconButton(
                            onClick = {
                                val formattedNumber =
                                    viewModel.formatPhoneNumberForWhatsApp(contact.numberPhone)
                                viewModel.sendWhatsAppMessage(
                                    context,
                                    formattedNumber,
                                    "Hola, necesito apoyo 😔"
                                )
                            },
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.whatsapp),
                                contentDescription = "Llamar",
                                tint = Color.White
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF34B7F1)),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        onClick = { viewModel.makeCall(context, contact.numberPhone)},
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.llamada),
                            contentDescription = "Llamar",
                            tint = Color.White
                        )
                    }
                }
            }




        }
    }
}