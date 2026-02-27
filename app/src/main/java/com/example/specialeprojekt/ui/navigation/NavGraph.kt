package com.example.specialeprojekt.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.specialeprojekt.ui.attestations.AttestationPage
import com.example.specialeprojekt.ui.login.LoginScreen
import com.example.specialeprojekt.ui.home.MainPage


// stores navigation for the application
@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Route.Login.route) {
        composable(Route.Login.route) { LoginScreen(navController) }
        composable(Route.Main.route) { MainPage(navController) }
        composable(Route.Attestation.route) { AttestationPage(navController) }
    }

}
