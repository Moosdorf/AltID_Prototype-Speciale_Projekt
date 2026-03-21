package com.example.specialeprojekt.ui.passport

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import org.jmrtd.BACKey
import org.jmrtd.lds.icao.MRZInfo

class PassportDataViewModel : ViewModel() {
    var bacKey: BACKey? by mutableStateOf(null)
    var tagConnected: Boolean by mutableStateOf(false)
    var mrzInfo: MRZInfo? by mutableStateOf(null)
    var passportPhoto: android.graphics.Bitmap? by mutableStateOf(null)
}
