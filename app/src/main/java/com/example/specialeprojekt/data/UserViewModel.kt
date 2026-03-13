package com.example.specialeprojekt.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.gson.Gson

class UserViewModel : ViewModel() {
    var username by mutableStateOf("")
    var attestations by mutableStateOf<Map<String, AttestationData>>(mapOf())

    fun addAttestation(attestation: AttestationData) {
        if (attestations.values.contains(attestation)) return

        attestations = attestations + (attestation.attestationType to attestation)
    }
}