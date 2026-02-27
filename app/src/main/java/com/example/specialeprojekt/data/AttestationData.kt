package com.example.specialeprojekt.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import java.util.Date

abstract class AttestationData {
    abstract val attestationType: String
    abstract val icon : ImageVector
    abstract val backGroundColor : Color

}

data class AldersBevis(
    override val attestationType: String = "Aldersbevis",
    val age : Int = 44,
    override val icon: ImageVector = Icons.Filled.Cake,
    override val backGroundColor: Color = Color(0xFF28BD77)
) : AttestationData()

data class LegitimationsBevis(
    override val attestationType: String = "Legitimationsbevis",
    val firstName : String = "Jakob",
    val lastName : String = "Smith",
    val dateOfBirth : String = "16-2-1982",
    val placeOfBirth : String = "Roskilde",
    val nationality : String = "Dansk",
    val address : String = "Nørrebrogade 42, 2200 København N",
    override val icon: ImageVector = Icons.Filled.AccountBox,
    override val backGroundColor: Color = Color(0xFF337EDC)
) : AttestationData()

data class SundhedsKort(
    override val attestationType: String = "Sundhedskort",
    val age : Int = 44,
    override val icon: ImageVector = Icons.Filled.CreditCard,
    override val backGroundColor: Color = Color(0xFFFFC25D)
) : AttestationData()



