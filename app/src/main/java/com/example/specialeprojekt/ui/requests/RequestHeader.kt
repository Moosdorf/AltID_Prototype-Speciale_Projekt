package com.example.specialeprojekt.ui.requests

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.specialeprojekt.ui.navigation.Route

@Composable
fun RequestHeader(navController: NavController) {
    Surface(color = Color.White) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left side - logo + title
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Home,
                    contentDescription = "logo",
                    tint = Color.Black
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("AltID")
            }

            // Right side - actions
            Row {
                // cancel button
                Button({
                    navController.navigate(Route.Main.route) {
                        popUpTo(0)
                    }
                }) {
                    Text("Afvis anmodning")
                }
                Spacer(modifier = Modifier.width(12.dp))
                Icon(modifier = Modifier.clickable {
                    navController.popBackStack()
                },
                    imageVector = Icons.Filled.Cancel,
                    contentDescription = "Cancel",
                    tint = Color.Black
                )

            }
        }
    }
}