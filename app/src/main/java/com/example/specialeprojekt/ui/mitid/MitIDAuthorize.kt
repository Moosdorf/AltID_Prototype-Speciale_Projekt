package com.example.specialeprojekt.ui.mitid

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.specialeprojekt.data.LegitimationsBevis
import com.example.specialeprojekt.data.UserViewModel
import com.example.specialeprojekt.ui.swiper.Swiper

@Composable
fun MitIDAuthorize(navController: NavController) {
    val context = LocalContext.current
    val mitIDRequestViewModel: MitIDRequestViewModel = viewModel(context as ComponentActivity)
    val userModel: UserViewModel = viewModel(context)




    Box(modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier.padding(horizontal = 32.dp)
        ) {
            Text("MitID")

            Text("Godkend: ${mitIDRequestViewModel.message}")

            Swiper {
                navController.navigate(mitIDRequestViewModel.path)
                if (mitIDRequestViewModel.message == "Identifikationsbevis") {
                    userModel.addAttestation(LegitimationsBevis())
                }
            }
        }
    }
}