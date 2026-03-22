package com.example.specialeprojekt.ui.passport

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import org.jmrtd.BACKey
import java.util.concurrent.Executors

@OptIn(androidx.camera.core.ExperimentalGetImage::class)
@Composable
fun MrzScannerScreen() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val passportData: PassportDataViewModel = viewModel(context as ComponentActivity)

    var flashOn by remember { mutableStateOf(false) }
    var camera by remember { mutableStateOf<androidx.camera.core.Camera?>(null) }

    val cutoutWidthDp  = 280.dp
    val cutoutHeightDp = 500.dp

    val recognizer = remember {
        TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    }

    val imageAnalyzer = remember {
        ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build().also { analysis ->
                analysis.setAnalyzer(Executors.newSingleThreadExecutor()) { imageProxy ->
                    val mediaImage = imageProxy.image
                    if (mediaImage != null) {
                        val image = InputImage.fromMediaImage(
                            mediaImage,
                            imageProxy.imageInfo.rotationDegrees
                        )
                        recognizer.process(image)
                            .addOnSuccessListener { visionText ->
                                val bACKey = extractBACFromMrz(visionText.text)
                                if (bACKey != null) {
                                    camera?.cameraControl?.enableTorch(false)
                                    passportData.bacKey = bACKey
                                    passportData.currentState = States.NFC

                                }
                            }
                            .addOnCompleteListener { imageProxy.close() }
                    } else {
                        imageProxy.close()
                    }
                }
            }
    }

    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { ctx ->
                val previewView = PreviewView(ctx)
                cameraProviderFuture.addListener({
                    val cameraProvider = cameraProviderFuture.get()
                    val preview = Preview.Builder().build().also {
                        it.surfaceProvider = previewView.surfaceProvider
                    }
                    cameraProvider.unbindAll()
                    camera = cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        CameraSelector.DEFAULT_BACK_CAMERA,
                        preview,
                        imageAnalyzer
                    )
                }, ContextCompat.getMainExecutor(ctx))
                previewView
            },
            modifier = Modifier.fillMaxSize()
        )


        Box(
            modifier = Modifier
                .width(cutoutWidthDp)
                .height(cutoutHeightDp)
                .align(Alignment.Center)
                .border(2.dp, Color.White, RectangleShape)
        )

        val density = LocalDensity.current
        Canvas(modifier = Modifier.fillMaxSize()) {
            val cutoutW = with(density) { cutoutWidthDp.toPx() }
            val cutoutH = with(density) { cutoutHeightDp.toPx() }
            val left = (size.width  - cutoutW) / 2f
            val top  = (size.height - cutoutH) / 2f

            // Dark scrim
            drawRect(color = Color(0x5EFFFFFF))


            // Clear the cutout to fully transparent
            drawRect(
                color     = Color.Transparent,
                topLeft   = Offset(left, top),
                size      = Size(cutoutW, cutoutH),
                blendMode = BlendMode.Clear,
            )

            // Border around the cutout
            drawRect(
                color     = Color.White,
                topLeft   = Offset(left, top),
                size      = Size(cutoutW, cutoutH),
                style     = Stroke(width = 4f)
            )


        }
        // MRZ zone border at the bottom third
        Box(
            modifier = Modifier
                .width(cutoutWidthDp / 4)
                .height(cutoutHeightDp)
                .offset(x = 40.dp,
                        y = 128.dp)
                .border(2.dp, Color.White, RectangleShape)
        )

        Text(
            text     = "Placer passet, så koden er i nederste felt",
            color    = Color.White,
            modifier = Modifier
                .align(Alignment.Center)
                .graphicsLayer { rotationZ = 90f }
        )

        // X button
        IconButton(
            onClick = {
                passportData.currentState = States.NONE
                passportData.startNFC = false
                passportData.tagConnected = false
            },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(start = 8.dp, top = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Luk",
                tint = Color.Black
            )
        }

        // flashlight
        Row(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(y = (-35).dp)
                .rotate(90f)
                .clickable {
                    flashOn = !flashOn
                    camera?.cameraControl?.enableTorch(flashOn)
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (flashOn) Icons.Default.FlashOn else Icons.Default.FlashOff,
                contentDescription = "flash",
                tint = Color.Black
            )
            Text(if (flashOn) "Lys på" else "Lys fra")
        }

        TextButton(
            onClick = { passportData.currentState = States.MANUAL },
            modifier = Modifier
                .align(Alignment.CenterStart)
                .offset(x = (-100).dp, y = (-140).dp)
                .rotate(90f)
        ) {
            Text("⌨ Indtast pasoplysninger manuelt", color = Color.Black)
        }
    }
}


fun extractBACFromMrz(text: String): BACKey? {
    val lines = text.lines().map { it.trim() }

    // trying to fetch last line in passport, mrz. it is 44 chars long.
    val td3 = lines.filter { it.length == 44 && it.matches(Regex("[A-Z0-9<]+")) }

    if (td3.isNotEmpty() && td3[0].length == 44) {
        val line = td3[0]
        val passNo = line.take(9)
        val birthDate = line.substring(13,19)
        val expiryDate = line.substring(21,27)

        Log.e("MRZ", passNo)
        Log.e("MRZ", birthDate)
        Log.e("MRZ", expiryDate)

        val bacKey = BACKey(passNo, birthDate, expiryDate)
        return bacKey
    }

    return null
}