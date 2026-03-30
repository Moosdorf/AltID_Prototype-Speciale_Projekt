package com.example.specialeprojekt.ui.attestations

import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.specialeprojekt.data.AttestationData
import com.example.specialeprojekt.ui.navigation.Route


@Composable
fun AttestationCard(data: AttestationData, navController: NavController) {
    val context = LocalContext.current
    val viewModel: AttestationPageViewModel = viewModel(context as ComponentActivity)

    Surface(modifier = Modifier.padding(10.dp),
            onClick = {
                viewModel.selectedAttestation = data
                navController.navigate(Route.Attestation.route)
                        }) {
        Box(modifier = Modifier
            .height(300.dp)
            .width(250.dp)
            .background(data.backGroundColor, RoundedCornerShape(12.dp))
            .padding(12.dp)) {

            Text(data.attestationType, color = Color.White)

            Icon(imageVector = data.icon,
                contentDescription = "Age proof",
                tint = Color.White,
                modifier = Modifier.align(Alignment.TopEnd)
                    .background(
                        Color(0x802C2B2B),
                        RoundedCornerShape(3.dp)))
            Icon(imageVector = Icons.Filled.QrCode2,
                contentDescription = "QR Scan",
                tint = Color.Black,
                modifier = Modifier.align(Alignment.BottomStart)
                    .background(Color.White, RoundedCornerShape(3.dp)))
        }
    }
}