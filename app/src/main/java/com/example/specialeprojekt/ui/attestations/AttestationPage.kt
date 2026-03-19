package com.example.specialeprojekt.ui.attestations

import android.os.Build
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.specialeprojekt.ui.home.AttestationPageHeader
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import com.example.specialeprojekt.ui.home.PassportImage

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AttestationPage(navController: NavController) {
    val context = LocalContext.current
    val data: AttestationPageViewModel = viewModel(context as ComponentActivity)



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 40.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
            .background(data.selectedAttestation?.backGroundColor ?: Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {


        var currentTime by remember { mutableStateOf("") }

        AttestationPageHeader(navController)
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween) {

            LaunchedEffect(Unit) {
                while (true) {
                    currentTime = LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern("HH:mm:ss"))
                    delay(1000) // update every second
                }
            }

            Text(currentTime)

            PassportImage(navController)


            // this will be invisible, used for structure with SpaceBetween in Row.
            Text(currentTime, color = data.selectedAttestation?.backGroundColor ?: Color.White)
        }

        Text("Showing: " + data.selectedAttestation?.attestationType)

        Spacer(modifier = Modifier.height(20.dp))


        when (data.selectedAttestation?.attestationType) {
            "Aldersbevis" -> AldersBevisComp()
            "Legitimationsbevis" -> LegitimationsBevisComp()
        }

    }


}
