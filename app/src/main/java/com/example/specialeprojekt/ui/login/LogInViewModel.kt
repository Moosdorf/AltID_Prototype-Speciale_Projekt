package com.example.specialeprojekt.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class LogInViewModel : ViewModel() {
    var currentState by mutableStateOf(LoginState.CREATE)
    var signedIn by mutableStateOf(false)
    var username by mutableStateOf("")
}