package com.example.specialeprojekt.ui.attestations

import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.specialeprojekt.data.SelectableAttribute
import com.example.specialeprojekt.data.UserViewModel
import com.example.specialeprojekt.ui.qr.QrCodeDisplay

@Composable
fun LegitimationsBevisComp() {
    val context = LocalContext.current
    val userData: UserViewModel = viewModel(context as ComponentActivity)
    val idProof = userData.attestations["Legitimationsbevis"] ?: return // if proof is null, return
    var selectableAttributes by remember {
        mutableStateOf(
            idProof.attributes.map { (k, v) -> SelectableAttribute(k, v, isSelected = true) }
        )
    }
    var showSelectAttributes by remember { mutableStateOf(false) }


    Column(
        modifier = Modifier
            .background(Color.White, RoundedCornerShape(10.dp))
            .width(300.dp)
            .padding(10.dp),  // no height — dynamic
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {

        val json: String = convertProofToJSON(selectableAttributes, "Legitimationsbevis")
        QrCodeDisplay(json)

        Text(
            "QR koden indeholder:",
            fontSize = 12.sp,
            modifier = Modifier
                .background(Color.Gray.copy(alpha = 0.1f), RoundedCornerShape(6.dp))
                .padding(horizontal = 6.dp, vertical = 2.dp)
        )

        val disclosedAttributes = selectableAttributes.filter { it.isSelected }

        // Name — centered, full width
        val fornavn = disclosedAttributes.find { it.key == "Fornavn" }?.value ?: ""
        val efternavn = disclosedAttributes.find { it.key == "Efternavn" }?.value ?: ""

        if (fornavn.isNotEmpty() || efternavn.isNotEmpty()) {
            Text(
                "Navn:",
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier
            )
            Text("$fornavn $efternavn", fontSize = 18.sp)
        }

        // Rest in pairs
        val otherAttributes =
            disclosedAttributes.filter { it.key != "Fornavn" && it.key != "Efternavn" }

        otherAttributes.chunked(2).forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                rowItems.forEach { attr ->
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(0.dp)
                    ) {
                        Text(
                            attr.key,
                            fontSize = 10.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text(
                            attr.value,
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }

        Button({ showSelectAttributes = true }) {
            Text("✎ Vælg selv, hvad du deler")
        }

        // dialog call
        if (showSelectAttributes) {
            SelectAttributesComposable(
                onDismiss = { showSelectAttributes = false },
                attributes = selectableAttributes,
                onToggle = { key ->
                    selectableAttributes = selectableAttributes.map {
                        if (it.key == key) it.copy(isSelected = !it.isSelected) else it
                    }
                }
            )
        }

    }
}

@Composable
fun SelectAttributesComposable(
    onDismiss: () -> Unit,
    attributes: List<SelectableAttribute>,
    onToggle: (String) -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Vælg attribut") },
        text = {
            Column {
                attributes.forEach { attr ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onToggle(attr.key) }
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(attr.key, modifier = Modifier.weight(1f))
                        if (attr.isSelected) {
                            Text(
                                attr.value,
                                color = Color.Gray,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.End
                            )
                        }
                        Checkbox(
                            checked = attr.isSelected,
                            onCheckedChange = null
                        )
                    }

                }
            }
        },
        confirmButton = {
            Button(onClick = { onDismiss() }) {
                Text("Ok")
            }
        }
    )
}