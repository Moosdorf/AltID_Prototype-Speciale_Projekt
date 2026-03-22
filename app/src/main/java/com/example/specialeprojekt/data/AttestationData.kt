package com.example.specialeprojekt.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import java.util.Date
import kotlin.String

abstract class AttestationData {
    abstract val attestationType: String
    abstract val icon : ImageVector
    abstract val backGroundColor : Color
    abstract val attributes : Map<String, String>

}

data class AldersBevis(
    override val attestationType: String = "Aldersbevis",
    val age : Int = 44,
    override val icon: ImageVector = Icons.Filled.Cake,
    override val backGroundColor: Color = Color(0xFF28BD77),
    override val attributes: Map<String, String> = mapOf(
        "Alder" to "44"
    )
) : AttestationData()

data class LegitimationsBevis(
    override val icon: ImageVector = Icons.Filled.AccountBox,
    override val backGroundColor: Color = Color(0xFF337EDC),
    override val attestationType: String = "Legitimationsbevis",
    override val attributes: Map<String, String> = mapOf(
        "Fornavn" to "Jakob",
        "Efternavn" to "Smith",
        "Fødselsdato" to "16-2-1982",
        "Fødselsted" to "Roskilde",
        "Nationalitet" to "Dansk",
        "Adresse" to "Nørrebrogade 42, 2200 København N",
    )
) : AttestationData()

data class SelectableAttribute(
    val key: String,
    val value: String,
    var isSelected: Boolean = true
)

val attributesMap = mapOf(
    "firstName" to "Fornavn",
    "lastName" to "Efternavn",
    "dateOfBirth" to "Fødselsdato",
    "placeOfBirth" to "Fødselsted",
    "nationality" to "Nationalitet",
    "address" to "Adresse",
    "attestationType" to "Bevistype",
    "age" to "Alder"
)

