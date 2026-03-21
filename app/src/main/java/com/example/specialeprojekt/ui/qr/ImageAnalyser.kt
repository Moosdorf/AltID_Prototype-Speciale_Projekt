package com.example.specialeprojekt.ui.qr

import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage


class ImageAnalyser(private val onQrScanned: (String) -> Unit) : ImageAnalysis.Analyzer {

    // scanner used to
    private val scanner = BarcodeScanning.getClient()

    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        // ?: is an if statement, if image is null, close image proxy
        val mediaImage = imageProxy.image ?: return imageProxy.close()

        val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

        scanner.process(image)
            .addOnSuccessListener { barcodes ->
                barcodes.firstOrNull()?.rawValue?.let { image -> onQrScanned(image) }
            }
            .addOnCompleteListener {
                imageProxy.close()
            }
    }
}