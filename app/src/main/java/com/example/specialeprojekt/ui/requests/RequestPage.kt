package com.example.specialeprojekt.ui.requests

import androidx.activity.ComponentActivity
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.specialeprojekt.data.RequestData
import com.example.specialeprojekt.ui.navigation.Route
import com.example.specialeprojekt.ui.qr.QRData
import com.example.specialeprojekt.ui.swiper.Swiper
import com.google.gson.Gson

@Composable
fun RequestPage(navController: NavController) {
    val context = LocalContext.current
    val qRModel: QRData = viewModel(context as ComponentActivity)
    val data = "" + qRModel.QRString

    val request = Gson().fromJson(data, RequestData::class.java)

    /* HEADER - LOGO venstre - AFVIS KNAP højre - KRYS højre */
    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(20.dp))
        RequestHeader(navController)
        Spacer(modifier = Modifier.height(180.dp))

        Text("Anmodning fra: " + request.relyingParty)
        Text("Bevis: " + request.attestationType)
        Spacer(modifier = Modifier.height(32.dp))
        Box(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                .padding(12.dp)
        ) {
            Text("Virksomheden spørger om dette data: " + request.attributesRequested)
        }
        Swiper({navController.navigate(Route.Main.route)})

    }





}