package com.example.specialeprojekt.ui.qr

import ImageAnalyser
import androidx.activity.ComponentActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Scan QR kode: ",
                modifier = Modifier.align(Alignment.TopCenter).padding(16.dp)
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

