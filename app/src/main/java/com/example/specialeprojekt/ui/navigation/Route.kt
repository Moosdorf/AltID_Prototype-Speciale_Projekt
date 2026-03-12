package com.example.specialeprojekt.ui.navigation

sealed class Route(val route: String) {
    object Login : Route("login")
    object Main : Route("main")
    object Attestation : Route("attestation")
    object QRScanner : Route("qrscan")
    object Request : Route("request")
    object MitIDAuth : Route("mitid")

}