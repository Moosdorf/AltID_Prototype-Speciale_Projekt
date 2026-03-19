package com.example.specialeprojekt.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.specialeprojekt.ui.attestations.AttestationPage
import com.example.specialeprojekt.ui.login.LoginScreen
import com.example.specialeprojekt.ui.home.MainPage
import com.example.specialeprojekt.ui.mitid.MitIDAuthorize
import com.example.specialeprojekt.ui.passport.PassportScannerPage
import com.example.specialeprojekt.ui.qr.QRPage
import com.example.specialeprojekt.ui.requests.RequestPage


// stores navigation for the application
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Route.Login.route) {
        composable(Route.Login.route) { LoginScreen(navController) }
        composable(Route.Main.route) { MainPage(navController) }
        composable(Route.Attestation.route) { AttestationPage(navController) }
        composable(Route.QRScanner.route) { QRPage(navController) }
        composable(Route.NFCScan.route) { PassportScannerPage(navController) }
        composable(Route.Request.route) { RequestPage(navController) }
        composable(Route.MitIDAuth.route) { MitIDAuthorize(navController) }

    }

}
