package com.example.specialeprojekt.ui.attestations

import android.content.Context.WINDOW_SERVICE
import android.graphics.Bitmap
import android.graphics.Point
import android.os.Build
import android.view.Display
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.specialeprojekt.R
import com.example.specialeprojekt.data.AldersBevis
import com.example.specialeprojekt.data.AttestationData
import com.example.specialeprojekt.data.LegitimationsBevis
import com.example.specialeprojekt.data.SundhedsKort
import com.example.specialeprojekt.data.UserViewModel
import com.example.specialeprojekt.ui.home.AttestationPageHeader
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import androidx.core.graphics.createBitmap
import androidx.core.graphics.set

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AttestationPage(navController: NavController) {
    val context = LocalContext.current
    val data: AttestationPageViewModel = viewModel(context as ComponentActivity)
    val userData: UserViewModel = viewModel(context as ComponentActivity)
    var selectedData: List<String> = listOf();

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
            val now = LocalDateTime.now()
            val formatted = now.format(DateTimeFormatter.ofPattern("HH:mm:ss"))

            LaunchedEffect(Unit) {
                while (true) {
                    currentTime = LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern("HH:mm:ss"))
                    delay(1000) // update every second
                }
            }

            Text(currentTime)

            Image(painter = painterResource(R.drawable.userpic),
                contentDescription = "user image",
                modifier = Modifier
                    .height(100.dp)
                    .width(100.dp)
            )

            // this will be invisible, used for structure with SpaceBetween in Row.
            Text(currentTime, color = data.selectedAttestation?.backGroundColor ?: Color.White)
        }
        Spacer(modifier = Modifier.height(20.dp))
        val proof = data.selectedAttestation
        Column(Modifier.background(Color.White, RoundedCornerShape(10.dp))
            .height(500.dp)
            .width(300.dp)
            .padding(10.dp)) {

            when (proof) {
                is AldersBevis -> {
                    selectedData = selectedData + proof.age.toString()
                }
                is LegitimationsBevis -> {
                    selectedData = selectedData + proof.firstName + proof.lastName + proof.dateOfBirth + proof.placeOfBirth + proof.nationality + proof.address
                }
                is SundhedsKort -> {
                    selectedData = selectedData + proof.age.toString()
                }
            }

            QrCodeDisplay(selectedData.joinToString("|"))

            Text("QR koden indenholder:")
            when (proof) {
                is AldersBevis -> {
                    Text("Alder: ${proof.age}")
                    selectedData = selectedData + proof.age.toString()
                }
                is LegitimationsBevis -> {
                    Text("Navn: ${proof.firstName} ${proof.lastName}")
                    Text("Fødselsdato: ${proof.dateOfBirth}")
                    Text("Fødested: ${proof.placeOfBirth}")
                    Text("Nationalitet: ${proof.nationality}")
                    Text("Adresse: ${proof.address}")

                    selectedData = selectedData + proof.firstName + proof.lastName + proof.dateOfBirth + proof.placeOfBirth + proof.nationality + proof.address
                }
                is SundhedsKort -> {
                    Text("Alder: ${proof.age}")
                    selectedData = selectedData + proof.age.toString()
                }

            }

        }
        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {}) {Text("Vælg hvad du vil dele")}
    }
}

// https://www.geeksforgeeks.org/kotlin/generate-qr-code-in-android-using-kotlin/

fun genQRCode(message: String, size: Int = 512): Bitmap { // returns Bitmap
    val bitMatrix = MultiFormatWriter().encode(message, BarcodeFormat.QR_CODE, size, size)
    val bitmap = createBitmap(size, size, Bitmap.Config.RGB_565)
    for (x in 0 until size)
        for (y in 0 until size)
            bitmap[x, y] =
                if (bitMatrix[x, y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE
    return bitmap
}

@Composable
fun QrCodeDisplay(message: String) {
    val qrBitmap = remember(message) { genQRCode(message) }

    Image(
        painter = BitmapPainter(qrBitmap.asImageBitmap()),
        contentDescription = "QR Code",
        modifier = Modifier.size(300.dp)
    )
}
