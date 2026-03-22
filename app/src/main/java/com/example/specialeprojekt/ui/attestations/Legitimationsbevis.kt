package com.example.specialeprojekt.ui.attestations

import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import com.example.specialeprojekt.data.UserViewModel
import com.example.specialeprojekt.ui.qr.QrCodeDisplay

@Composable
fun LegitimationsBevisComp() {
    val context = LocalContext.current
    val userData: UserViewModel = viewModel(context as ComponentActivity)
    val idProof = userData.attestations["Legitimationsbevis"] ?: return // if proof is null, return
    var selectedAttributes by remember { mutableStateOf(idProof.attributes)}
    var showSelectAttributes by remember { mutableStateOf(false) }


    Column(
        modifier = Modifier
            .background(Color.White, RoundedCornerShape(10.dp))
            .width(300.dp)
            .padding(10.dp),  // no height — dynamic
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        QrCodeDisplay("monkey")

        Text(
            "QR koden indeholder:",
            fontSize = 12.sp,
            modifier = Modifier
                .background(Color.Gray.copy(alpha = 0.1f), RoundedCornerShape(6.dp))
                .padding(horizontal = 6.dp, vertical = 2.dp)
        )

        Text(
            "Navn:",
            fontSize = 12.sp,
            modifier = Modifier
                .background(Color.Gray.copy(alpha = 0.1f), RoundedCornerShape(6.dp))
                .padding(horizontal = 6.dp, vertical = 2.dp)
        )
        Text(
            idProof.attributes["Fornavn"] + " " + idProof.attributes["Efternavn"],
            fontSize = 12.sp
        )

        val entries = selectedAttributes.entries.toList().drop(2)
        entries.chunked(2).forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                rowItems.forEach { (key, value) ->
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Text(
                            key,
                            fontSize = 12.sp,
                            modifier = Modifier
                                .background(Color.Gray.copy(alpha = 0.1f), RoundedCornerShape(6.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                        Text(value, fontSize = 12.sp)
                    }
                }
            }
        }
    }

    Button({ showSelectAttributes = true } ) {
        Text("✎ Vælg selv, hvad du deler")
    }

    // dialog outside the column
    if (showSelectAttributes ) {
        SelectAttributes({ showSelectAttributes = false }, idProof.attributes)
    }

}


@Composable
fun SelectAttributes(onDismiss: () -> Unit, attributes: Map<String, String>) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Vælg attribut") },
        text = {
            Column {
                attributes.forEach { (key, value) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                // onDismiss()
                            }
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(key, modifier = Modifier.weight(1f))
                        Text(value, color = Color.Gray)
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = { onDismiss() }) {
                Text("Annuller")
            }
        }
    )
}