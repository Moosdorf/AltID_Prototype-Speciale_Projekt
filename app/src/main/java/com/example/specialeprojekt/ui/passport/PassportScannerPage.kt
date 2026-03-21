package com.example.specialeprojekt.ui.passport
import android.app.PendingIntent
import android.content.Intent
import android.nfc.NfcAdapter
import android.os.Build
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SegmentedButtonDefaults.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.specialeprojekt.R
import com.example.specialeprojekt.data.UserViewModel
import com.example.specialeprojekt.ui.navigation.Route
import org.jmrtd.BACKey
import java.time.ZoneId
import java.util.Calendar
import java.util.TimeZone
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

private enum class States { NONE, MANUAL, SCAN }
@Composable
fun PassportScannerPage(navController: NavController) {
    val context = LocalContext.current
    val activity = context as ComponentActivity
    val userModel: PassportDataViewModel = viewModel(activity)

    var state by remember { mutableStateOf(States.NONE) }






    DisposableEffect(userModel.bacKey) {
        if (userModel.bacKey == null) return@DisposableEffect onDispose {} // if not ready to start scanning nfc

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
    LaunchedEffect(userModel.passportPhoto) {
        if (userModel.passportPhoto != null) {
            navController.navigate(Route.Main.route)
        }
    }


    Box(modifier = Modifier.fillMaxSize()
                    .padding(top = 48.dp)) {
        Text("Back", modifier = Modifier.clickable {
            if (state == States.NONE) {
                navController.navigate(Route.Main.route) {
                    state = States.NONE
                    popUpTo(0)
                }
            } else {
                state = States.NONE
            }

        })
        when {
            state == States.NONE -> {
                Image(
                    painter = painterResource(R.drawable.passcanmrz),
                    contentDescription = "passcanmrz",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }

        Column(modifier = Modifier
            .align(Alignment.BottomCenter)
            .fillMaxWidth()
            .padding(horizontal = 28.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)) {
            when {
                userModel.bacKey != null -> {
                    ScanNFCPage(userModel)
                }
                state == States.NONE -> {

                    Text("⌨ Indtast pasoplysninger manuelt",
                        color = Color.Blue,
                        modifier = Modifier.background(Color.White)
                            .clickable{
                                 state = States.MANUAL
                            }
                    )

                    Button(
                        onClick = { state = States.SCAN},
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
                state == States.MANUAL -> {
                    ManualPickBAC()
                }
                state == States.SCAN -> {

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
    var passportData: PassportDataViewModel = viewModel(activity)

    var showBirthdayPick by remember { mutableStateOf(false) }
    var showExpiryPick by remember { mutableStateOf(false) }

    val datePickerStateBirthdate = rememberDatePickerState()
    val datePickerStateExpiry = rememberDatePickerState()

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

    Text(passportNo)
    Text(birthDate)
    Text(expiryDate)

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
            label = { Text("FØDSELSDATO (DD/MM/ÅÅ)") },
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
            label = { Text("UDLØBSDATO (DD/MM/ÅÅ)") },
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
        } catch (e: Exception) {
            Log.e("PASSPORT", "Cannot create BAC key from attributes")
        }
    }) {
        Text("Ok")
    }

}

@Composable
fun ScanNFCPage(passportData: PassportDataViewModel) {
    when {
        passportData.passportPhoto != null -> {
            // Won't be visible long since LaunchedEffect navigates away
            Text("Billede overført!")
        }
        passportData.mrzInfo != null -> {
            // MRZ read, now reading photo
            CircularProgressIndicator(modifier = Modifier.size(64.dp))
            Spacer(modifier = Modifier.height(16.dp))
            Text("Indlæser billede...")
        }
        passportData.tagConnected -> {
            CircularProgressIndicator(modifier = Modifier.size(64.dp))
            Spacer(modifier = Modifier.height(16.dp))
            Text("Pas fundet...")
        }
        else -> {
            Spacer(modifier = Modifier.height(24.dp))
            Text("Hold telefonen tæt på dit pas.")
        }
    }
}