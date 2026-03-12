package com.example.specialeprojekt.data

data class RequestData (
    val relyingParty: String,
    val attestationType: String,
    val attributesRequested: List<String>
)

