package com.example.specialeprojekt.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.gson.Gson

class UserViewModel : ViewModel() {
    var username by mutableStateOf("")
    var attestations by mutableStateOf(listOf<AttestationData>())


    fun addAttestation(attestation: AttestationData) {
        if (attestations.contains(attestation)) return;

        attestations = attestations + attestation
    }
}