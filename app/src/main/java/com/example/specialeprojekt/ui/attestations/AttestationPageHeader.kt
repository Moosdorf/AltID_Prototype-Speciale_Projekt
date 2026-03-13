package com.example.specialeprojekt.ui.home

import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.specialeprojekt.R
import com.example.specialeprojekt.ui.attestations.AttestationPageViewModel

@Composable
fun AttestationPageHeader(navController: NavController) {
    val context = LocalContext.current
    val data: AttestationPageViewModel = viewModel(context as ComponentActivity)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .background(data.selectedAttestation?.backGroundColor ?: MaterialTheme.colorScheme.background),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Left side
        Image(
            painter = painterResource(R.drawable.digst),
            contentDescription = "digst logo",
            modifier = Modifier.height(20.dp).width(20.dp)
        )
        // Center
        Row {
            Text(data.selectedAttestation?.attestationType ?: "Bevistype")
        }

        // Right side
        Row {
            Icon(
                imageVector = Icons.Filled.Cancel,
                contentDescription = "X",
                tint = Color.Black,
                modifier = Modifier.clickable {
                    navController.popBackStack()
                }
            )
        }
    }
}