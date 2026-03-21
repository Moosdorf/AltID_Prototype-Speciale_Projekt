package com.example.specialeprojekt.ui.qr

import androidx.activity.ComponentActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.specialeprojekt.ui.navigation.Route
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import java.util.concurrent.Executors
import com.google.accompanist.permissions.isGranted


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun QRPage(navController: NavController) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val permissionState = rememberPermissionState(android.Manifest.permission.CAMERA)

    val context = LocalContext.current
    val qRModel: QRData = viewModel(context as ComponentActivity)

    var qrScanned by remember { mutableStateOf(false) }




    LaunchedEffect(Unit) {
        permissionState.launchPermissionRequest()
    }
    Box(modifier = Modifier.fillMaxSize()) {
        if (permissionState.status.isGranted) {
            AndroidView(
                factory = { ctx ->
                    val previewView = PreviewView(ctx)
                    val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)

                    cameraProviderFuture.addListener({
                        val cameraProvider = cameraProviderFuture.get()
                        val preview = Preview.Builder().build().also {
                            it.surfaceProvider = previewView.surfaceProvider
                        }
                        val analyzer = ImageAnalysis.Builder().build().also {
                            it.setAnalyzer(Executors.newSingleThreadExecutor(), ImageAnalyser({ e ->
                                qrScanned = true
                                navController.navigate(Route.Request.route)
                                qRModel.QRString = e
                        }))
                        }
                        // check if unbind works when nothing is bound already. otherwise i need to use this whenever there is something bound.
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(lifecycleOwner, CameraSelector.DEFAULT_BACK_CAMERA, preview, analyzer)
                    }, ContextCompat.getMainExecutor(ctx))

                    previewView
                },
                modifier = Modifier.fillMaxSize()
            )
            val scanBoxSize = 250.dp

            // Dark overlay with a transparent hole in the middle
            Canvas(modifier = Modifier.fillMaxSize()) {
                val boxPx = scanBoxSize.toPx()
                val left = (size.width - boxPx) / 2
                val top = (size.height - boxPx) / 2

                // Dark overlay
                drawRect(color = Color(0x99000000))

                // Cut out the center square
                drawRect(
                    color = Color.Transparent,
                    topLeft = Offset(left, top),
                    size = Size(boxPx, boxPx),
                    blendMode = BlendMode.Clear
                )
            }

            // Border around the scan box
            Box(
                modifier = Modifier
                    .size(scanBoxSize)
                    .align(Alignment.Center)
                    .border(2.dp, if (qrScanned) Color.Green else Color.Red, RectangleShape)
            )

            Text(
                text = "Scan QR kode",
                modifier = Modifier.align(Alignment.TopCenter).padding(top = 100.dp),
                color = Color.White
            )

            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp)
            ) {
                Text("Annuller")
            }

        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Kamera tilladelse er krævet")
            }
        }
    }
}

