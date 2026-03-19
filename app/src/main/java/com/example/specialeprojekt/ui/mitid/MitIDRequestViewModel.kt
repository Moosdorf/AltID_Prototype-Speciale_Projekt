package com.example.specialeprojekt.ui.mitid

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class MitIDRequestViewModel : ViewModel() {
    var message by mutableStateOf("")
    var path by mutableStateOf("")

}