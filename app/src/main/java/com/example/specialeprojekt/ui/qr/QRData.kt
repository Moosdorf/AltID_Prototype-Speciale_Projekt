package com.example.specialeprojekt.ui.qr

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class QRData : ViewModel() {
    var QRString by mutableStateOf<String?>(null)

    fun onQRScanned(text: String) {
        QRString = text
    }

}