package com.example.specialeprojekt.ui.requests

import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.specialeprojekt.data.RequestData
import com.example.specialeprojekt.data.UserViewModel
import com.example.specialeprojekt.data.attributesMap
import com.example.specialeprojekt.ui.navigation.Route
import com.example.specialeprojekt.ui.qr.QRData
import com.example.specialeprojekt.ui.swiper.Swiper
import com.google.gson.Gson

@Composable
fun RequestPage(navController: NavController) {
    val context = LocalContext.current
    val userModel: UserViewModel = viewModel(context as ComponentActivity)
    val qRModel: QRData = viewModel(context)
    val data = "" + qRModel.QRString

    val request = Gson().fromJson(data, RequestData::class.java)
    var isShared by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 20.dp)
        ) {
            RequestHeader(navController, isShared)
        }


        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 32.dp)
        ) {
            Text("Anmodning fra: " + request.relyingParty)
            Text("Bevis: " + request.attestationType)

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = if (isShared) "Data delt!" else "Del data:",
                modifier = Modifier
                    .background(
                        color = if (isShared) Color(92, 184, 92) else Color.White,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                    .padding(24.dp)
            ) {
                Column {
                    val attestation = userModel.attestations[request.attestationType]
                    if (attestation != null) {
                        for (attr in request.attributesRequested) {
                            val displayName = attributesMap[attr] ?: attr
                            val value = attestation.attributes[displayName]
                            Text("$displayName: $value")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Box(
                Modifier
                    .border(1.dp, Color.Gray, RoundedCornerShape(12.dp))
                    .background(Color(0x17000002), RoundedCornerShape(12.dp))
                    .padding(2.dp)
            ) {
                Text("✓ Du deler ingen andre oplysninger.", fontSize = 8.sp)
            }
        }


        // Swiper at the bottom
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 48.dp)
        ) {
            if (isShared) {
                Button({
                    navController.navigate(Route.Main.route) {
                        isShared = false
                        popUpTo(0)
                    }
                }) {
                    Icon(tint = Color(76, 175, 80, 255),
                        imageVector = Icons.Filled.Check,
                        contentDescription = "Shared"
                    )
                }

            } else {
                Swiper {
                    isShared = true
                }
            }
        }
    }
}