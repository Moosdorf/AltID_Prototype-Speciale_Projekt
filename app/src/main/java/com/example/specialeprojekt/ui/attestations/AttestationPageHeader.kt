package com.example.specialeprojekt.ui.attestations

import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.specialeprojekt.data.UserViewModel
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.TextButton
import com.example.specialeprojekt.ui.navigation.Route

@Composable
fun AttestationPageHeader(navController: NavController) {
    val context = LocalContext.current
    val data: AttestationPageViewModel = viewModel(context as ComponentActivity)
    val userViewModel: UserViewModel = viewModel(context)

    var showMenu by remember { mutableStateOf(false) }
    var showConfirmDialog by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .background(data.selectedAttestation?.backGroundColor ?: MaterialTheme.colorScheme.background),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Left — "..."
        Box {
            Text(
                text = "···",
                modifier = Modifier.clickable { showMenu = !showMenu }
            )
            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Fjern bevis", color = MaterialTheme.colorScheme.error) },
                    onClick = {
                        showMenu = false
                        showConfirmDialog = true
                    }
                )
            }
        }

        // Center
        Text(data.selectedAttestation?.attestationType ?: "Bevistype")

        // Right
        Icon(
            imageVector = Icons.Filled.Cancel,
            contentDescription = "Luk",
            tint = Color.Black,
            modifier = Modifier.clickable { navController.popBackStack() }
        )
    }

    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text("Fjern bevis") },
            text = { Text("Er du sikker på, at du vil fjerne ${data.selectedAttestation?.attestationType}?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        userViewModel.removeAttestation(data.selectedAttestation?.attestationType)
                        showConfirmDialog = false
                        navController.navigate(Route.Main.route) {
                            popUpTo(0)
                        }
                    }
                ) {
                    Text("Ja", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDialog = false }) {
                    Text("Nej")
                }
            }
        )
    }
}