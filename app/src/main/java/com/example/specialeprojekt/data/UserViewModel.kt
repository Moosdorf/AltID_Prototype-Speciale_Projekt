package com.example.specialeprojekt.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class UserViewModel : ViewModel() {
    var username by mutableStateOf("")
    var showPassportImage by mutableStateOf(false)
    var attestations by mutableStateOf<Map<String, AttestationData>>(mapOf())
    var passportPhoto: android.graphics.Bitmap? by mutableStateOf(null)

    fun addAttestation(attestation: AttestationData) {
        if (attestations.values.contains(attestation)) return

        attestations = attestations + (attestation.attestationType to attestation)
    }

    fun removeAttestation(attestationType: String?) {
        if (attestationType != null && attestations.keys.contains(attestationType))
            attestations = attestations - attestationType
    }
}