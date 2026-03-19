package com.example.specialeprojekt.ui.passport
import android.app.PendingIntent
import android.content.Intent
import android.nfc.NfcAdapter
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.specialeprojekt.ui.navigation.Route

@Composable
fun PassportScannerPage(navController: NavController) {
    val context = LocalContext.current
    val activity = context as ComponentActivity
    val userModel: PassportDataViewModel = viewModel(activity)


    DisposableEffect(Unit) {
        val nfcAdapter = NfcAdapter.getDefaultAdapter(activity)
        val intent = Intent(activity, activity.javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        val pending = PendingIntent.getActivity(
            activity, 0, intent,
            PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        nfcAdapter.enableForegroundDispatch(activity, pending, null, null)
        onDispose { nfcAdapter.disableForegroundDispatch(activity) }
    }

    // navigate to main when passport photo is obtained
    LaunchedEffect(userModel.passportPhoto) {
        if (userModel.passportPhoto != null) {
            navController.navigate(Route.Main.route)
        }
    }


    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            when {
                userModel.passportPhoto != null -> {
                    // Won't be visible long since LaunchedEffect navigates away
                    Text("Billede overført!")
                }
                userModel.mrzInfo != null -> {
                    // MRZ read, now reading photo
                    CircularProgressIndicator(modifier = Modifier.size(64.dp))
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Indlæser billede...")
                }
                userModel.tagConnected -> {
                    CircularProgressIndicator(modifier = Modifier.size(64.dp))
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Pas fundet...")
                }
                else -> {
                    Spacer(modifier = Modifier.height(24.dp))
                    Text("Hold telefonen tæt på dit pas.")
                }
            }
        }
    }
}