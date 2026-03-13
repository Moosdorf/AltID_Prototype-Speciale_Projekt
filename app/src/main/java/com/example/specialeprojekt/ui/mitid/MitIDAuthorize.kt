package com.example.specialeprojekt.ui.mitid

import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.specialeprojekt.R
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
            Image(
                painter = painterResource(R.drawable.mitid),
                contentDescription = "user image",
                modifier = Modifier.height(200.dp).width(200.dp)
            )

            Text("MitID")
            Spacer(modifier = Modifier.height(80.dp))

            Text("Godkend: ${mitIDRequestViewModel.message}")
            Spacer(modifier = Modifier.weight(1f))

            Swiper {
                navController.navigate(mitIDRequestViewModel.path) {
                    popUpTo(0)
                }
                if (mitIDRequestViewModel.message == "Legitimationsbevis") {
                    userModel.addAttestation(LegitimationsBevis())
                }
            }
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}