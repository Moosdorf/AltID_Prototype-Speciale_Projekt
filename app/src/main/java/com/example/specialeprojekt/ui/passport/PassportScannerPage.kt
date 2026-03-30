package com.example.specialeprojekt.ui.passport
import android.app.PendingIntent
import android.content.Intent
import android.nfc.NfcAdapter
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.specialeprojekt.R
import com.example.specialeprojekt.data.UserViewModel
import com.example.specialeprojekt.ui.navigation.Route
import org.jmrtd.BACKey
import java.util.Calendar
import java.util.TimeZone
import kotlin.time.ExperimentalTime

enum class States { NONE, MANUAL, SCAN, NFC }
@Composable
fun PassportScannerPage(navController: NavController) {
    val context = LocalContext.current
    val activity = context as ComponentActivity
    val passportData: PassportDataViewModel = viewModel(activity)
    val userViewModel: UserViewModel = viewModel(activity)



    DisposableEffect(passportData.startNFC) {
        if (!passportData.startNFC) return@DisposableEffect onDispose {} // if not ready to start scanning nfc

        val nfcAdapter = NfcAdapter.getDefaultAdapter(activity)
        val intent = Intent(activity, activity.javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        val pending = PendingIntent.getActivity(
            activity, 0, intent,
            PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        nfcAdapter.enableForegroundDispatch(activity, pending, null, null)
        onDispose { nfcAdapter.disableForegroundDispatch(activity) }
    }

    // navigate to main when passport photo is obtained
    LaunchedEffect(userViewModel.passportPhoto) {
        if (userViewModel.passportPhoto != null) {
            navController.navigate(Route.Main.route) {
                popUpTo(0)
            }

        }
    }

        Box(modifier = Modifier.fillMaxSize()
                        .padding(top = 48.dp)) {

            if (passportData.currentState != States.NFC) {
                IconButton(
                    onClick = {
                        passportData.startNFC = false
                        passportData.tagConnected = false

                        if (passportData.currentState == States.NONE) {
                            navController.navigate(Route.Main.route) {
                                popUpTo(0)
                            }
                        } else {
                            passportData.currentState = States.NONE
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(start = 8.dp, top = 8.dp)
                        .offset(x = (-15).dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Luk",
                        tint = Color.Black
                    )
            }
        }

        // these will fill whole screen
        when (passportData.currentState) {
            States.SCAN -> { // to go to camera state to scan mrz line
                MrzScannerScreen()
            }
            States.NONE -> { // provide guiding image 60% of screen
                Image(
                    painter = painterResource(R.drawable.passcanmrz),
                    contentDescription = "passcanmrz",
                    modifier = Modifier.align(Alignment.TopCenter)
                        .padding(top = 140.dp)
                )
            }
            States.NFC -> { // provide guiding image 60% of screen
                Image(
                    painter = painterResource(R.drawable.nfcchipscan),
                    contentDescription = "passcanmrz",
                    modifier = Modifier.align(Alignment.TopCenter)
                        .padding(top = 140.dp)
                        .size(300.dp)
                        .rotate(30f)
                )
                ScanNFCPage()
            }
            States.MANUAL -> { // form for BAC data
                Column(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .fillMaxWidth()
                        .padding(start = 28.dp, end = 28.dp, top = 80.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ManualPickBAC()
                }
            }

        }


        Column(modifier = Modifier
            .align(Alignment.BottomCenter)
            .fillMaxWidth()
            .padding(horizontal = 28.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)) {

            if (passportData.currentState == States.NONE) {

                // options when no states
                Text("⌨ Indtast pasoplysninger manuelt",
                    color = Color(0xFF0060E6),
                    modifier = Modifier.background(Color(0xFFFFFBFE))
                        .clickable{
                            passportData.currentState = States.MANUAL
                        }
                )

                Button(
                    onClick = { passportData.currentState = States.SCAN},
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            "SCAN KODEN",
                            modifier = Modifier.align(Alignment.CenterStart)
                        )
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = null,
                            modifier = Modifier.align(Alignment.CenterEnd)
                        )
                    }
                }
            }

        }
    }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
fun ManualPickBAC() {
    val context = LocalContext.current
    val activity = context as ComponentActivity
    val passportData: PassportDataViewModel = viewModel(activity)

    var showBirthdayPick by remember { mutableStateOf(false) }
    var showExpiryPick by remember { mutableStateOf(false) }

    val datePickerStateBirthdate = rememberDatePickerState()
    val datePickerStateExpiry = rememberDatePickerState()

    var error by remember { mutableStateOf(false) }

    var passportNo by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var expiryDate by remember { mutableStateOf("") }

    fun millisToDate(millis: Long): String {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
            timeInMillis = millis
        }
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH) + 1 // Calendar.MONTH is 0-indexed
        val year = calendar.get(Calendar.YEAR) % 100
        return "%02d%02d%02d".format(year, month, day)
    }

    if (error) Text("Fejl: Oplysninger er ikke gyldige", color = Color.Red)
    Text("Indtast pasoplysninger")

    // Passport number
    TextField(
        value = passportNo,
        onValueChange = { if (it.length <= 9 && it.all { c -> c.isDigit() }) passportNo = it },
        label = { Text("PASNUMMER") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier.fillMaxWidth()
    )

    // Birthdate
    Box(modifier = Modifier.fillMaxWidth()) {
        TextField(
            value = birthDate,
            onValueChange = {},
            readOnly = true,
            label = { Text("FØDSELSDATO (DD/MM/ÅÅÅÅ)") },
            modifier = Modifier.fillMaxWidth()
        )
        Box(modifier = Modifier
            .matchParentSize()
            .clickable { showBirthdayPick = true }
        )
    }

    if (showBirthdayPick) {
        DatePickerDialog(
            onDismissRequest = { showBirthdayPick = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerStateBirthdate.selectedDateMillis?.let {
                        birthDate = millisToDate(it)
                    }
                    showBirthdayPick = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showBirthdayPick = false }) { Text("Annuller") }
            }
        ) {
            DatePicker(state = datePickerStateBirthdate)
        }
    }

    // Expiry date
    Box(modifier = Modifier.fillMaxWidth()) {
        TextField(
            value = expiryDate,
            onValueChange = {},
            readOnly = true,
            label = { Text("UDLØBSDATO (DD/MM/ÅÅÅÅ)") },
            modifier = Modifier.fillMaxWidth()
        )
        Box(modifier = Modifier
            .matchParentSize()
            .clickable { showExpiryPick = true }
        )
    }

    if (showExpiryPick) {
        DatePickerDialog(
            onDismissRequest = { showExpiryPick = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerStateExpiry.selectedDateMillis?.let {
                        expiryDate = millisToDate(it)
                    }
                    showExpiryPick = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showExpiryPick = false }) { Text("Annuller") }
            }
        ) {
            DatePicker(state = datePickerStateExpiry)
        }
    }

    Button({
        try {
            passportData.bacKey = BACKey(passportNo, birthDate, expiryDate)
            passportData.currentState = States.NFC
        } catch (e: Exception) {
            error = true
            Log.e("PASSPORT", "Cannot create BAC key from attributes")
        }
    }) {
        Text("Ok")
    }

}

@Composable
fun ScanNFCPage() {
    val context = LocalContext.current
    val activity = context as ComponentActivity
    val passportData: PassportDataViewModel = viewModel(activity)
    val userViewModel: UserViewModel = viewModel(activity)

    Box(modifier = Modifier.fillMaxSize()) {
        // to add dark color all screen
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x66000000))
        )
        IconButton(
            onClick = {
                passportData.currentState = States.NONE
                passportData.startNFC = false
                passportData.tagConnected = false
            },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(start = 8.dp, top = 8.dp)
                .offset(x = (-15).dp)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Luk",
                tint = Color.White
            )
        }
        // 40% not dark
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .fillMaxHeight(0.4f)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                )
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
                    .padding(all = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,

            ) {
                when {
                    userViewModel.passportPhoto != null -> {
                        Text("Billede overført!")
                    }
                    passportData.mrzInfo != null -> {
                        CircularProgressIndicator(modifier = Modifier.size(64.dp))
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Indlæser billede...")
                    }
                    passportData.tagConnected -> {
                        CircularProgressIndicator(modifier = Modifier.size(64.dp))
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Pas fundet...")
                    }
                    passportData.startNFC -> {
                        if (passportData.failed) Text("OBS: Der var fejl i scanning. Prøv igen.", color = Color.Red)
                        Spacer(modifier = Modifier.height(24.dp))
                        Text("Klar til at scanne")
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                    else -> {
                        Text("Læg telefonen oven på dit pas for at aflæse chippen.")
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { passportData.startNFC = true },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                        ) {
                            Box(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    "AFLÆS CHIP",
                                    modifier = Modifier.align(Alignment.CenterStart)
                                )
                                Icon(
                                    imageVector = Icons.Default.CameraAlt,
                                    contentDescription = null,
                                    modifier = Modifier.align(Alignment.CenterEnd)
                                )
                            }
                        }
                    }
                }
                Button({
                    passportData.startNFC = false
                    passportData.currentState = States.NONE
                },
                    modifier = Modifier.offset(y = 40.dp)) {
                    Text("Annuller")
                }
            }
        }
    }
}