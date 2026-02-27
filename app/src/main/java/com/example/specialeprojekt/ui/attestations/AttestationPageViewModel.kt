package com.example.specialeprojekt.ui.attestations

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.specialeprojekt.data.AttestationCardData
import com.example.specialeprojekt.data.AttestationData

class AttestationPageViewModel : ViewModel() {
    var selectedAttestation by mutableStateOf<AttestationData?>(null)
}