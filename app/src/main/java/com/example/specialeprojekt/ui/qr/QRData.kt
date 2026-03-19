package com.example.specialeprojekt.ui.qr

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.unit.dp
import androidx.core.graphics.createBitmap
import androidx.core.graphics.set
import androidx.lifecycle.ViewModel
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter

class QRData : ViewModel() {
    var QRString by mutableStateOf<String?>(null)

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
    if (message.isBlank()) {
        Text("No QR code available.")
        return
    }

    val qrBitmap = remember(message) { genQRCode(message) }

    Image(
        painter = BitmapPainter(qrBitmap.asImageBitmap()),
        contentDescription = "QR Code",
        modifier = Modifier.size(200.dp)
    )
}