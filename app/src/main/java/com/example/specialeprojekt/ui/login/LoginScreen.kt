package com.example.specialeprojekt.ui.login

import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.specialeprojekt.R
import com.example.specialeprojekt.data.UserViewModel
import com.example.specialeprojekt.ui.mitid.MitIDRequestViewModel
import com.example.specialeprojekt.ui.navigation.Route
import com.example.specialeprojekt.ui.passport.States

@Composable
fun LoginScreen(navController: NavController) {
    var username by remember { mutableStateOf("") }
    val context = LocalContext.current
    val userViewModel: UserViewModel = viewModel(context as ComponentActivity)


    val mitIDRequestViewModel: MitIDRequestViewModel = viewModel(context)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(R.drawable.digst),
            contentDescription = "user image",
            modifier = Modifier.height(200.dp).width(200.dp)
        )

        Text("Opret AltID konto", fontSize = 24.sp)

        Spacer(modifier = Modifier.height(32.dp))

        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                userViewModel.username = username
                navController.navigate(Route.MitIDAuth.route) {
                    mitIDRequestViewModel.path = Route.Main.route
                    mitIDRequestViewModel.message = "Registrér AltID"
                }
            },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text("Registrér med MitID")
        }

    }
}