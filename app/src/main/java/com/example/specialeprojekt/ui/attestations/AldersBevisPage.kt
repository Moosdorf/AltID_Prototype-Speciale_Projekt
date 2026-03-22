package com.example.specialeprojekt.ui.attestations

import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
fun AldersBevisComp() {
    val context = LocalContext.current
    val userData: UserViewModel = viewModel(context as ComponentActivity)
    val ageProof = userData.attestations["Aldersbevis"] ?: return // if proof is null, return
    val currentAge = ageProof.attributes["Alder"]?.toInt() ?: return
    var showSelectAttributes by remember { mutableStateOf(false) }

    var agesEligible = mapOf<Int, Boolean>()
    agesEligible = agesEligible + mapOf(
        13 to (currentAge >= 13),
        15 to (currentAge >= 15),
        16 to (currentAge >= 16),
        18 to (currentAge >= 18),
        21 to (currentAge >= 21),
        23 to (currentAge >= 23),
        45 to (currentAge >= 45),
        65 to (currentAge >= 65),
        67 to (currentAge >= 67),
        75 to (currentAge >= 75),
    )
    val agePlus = remember { mutableIntStateOf(if (currentAge >= 18) 18 else 13) }

    // dialog outside the column
    if (showSelectAttributes ) {
        SelectAttributesComposable({ showSelectAttributes = false }, agesEligible, agePlus)
    }


    Column(
        modifier = Modifier
            .background(Color.White, RoundedCornerShape(10.dp))
            .height(400.dp)
            .width(300.dp)
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally  // ← add this
    ) {

        QrCodeDisplay("{\n" +
                "  \"attestationType\": \"Aldersbevis\",\n" +
                "  \"ageAbove\": {\"${agePlus.intValue}\":\"${currentAge >= agePlus.intValue}\"}\n" +
                "}")

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 2.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("QR koden indeholder:",
                modifier = Modifier
                    .background(Color.Gray.copy(alpha = 0.1f), RoundedCornerShape(6.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp))
            Spacer(modifier = Modifier.height(18.dp))
            Text("Aldersgrænse:",
                modifier = Modifier
                    .background(Color.Gray.copy(alpha = 0.1f), RoundedCornerShape(6.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp))
            Text("${agePlus.intValue}+", fontSize = 18.sp)
        }
    }

    Button({ showSelectAttributes = true },
        modifier = Modifier.padding(top = 80.dp) ) {
        Text("✎ Vælg selv, hvad du deler")
    }

}

@Composable
fun SelectAttributesComposable(onDismiss: () -> Unit, attributes: Map<Int, Boolean>, agePlus: MutableIntState) {
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
                                agePlus.intValue = key
                                onDismiss()
                            }
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(""+key, modifier = Modifier.weight(1f))
                        Text(""+value, color = Color.Gray)
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