package com.example.specialeprojekt.data

import android.R
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class AttestationCardData(
    val attestationName : String,
    val icon : ImageVector,
    val backGroundColor : Color,
    val userHasAttestation : Boolean
)