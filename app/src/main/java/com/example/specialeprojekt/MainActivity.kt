package com.example.specialeprojekt

import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.IsoDep
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.specialeprojekt.theme.SpecialeProjektTheme
import com.example.specialeprojekt.ui.navigation.NavGraph
import com.example.specialeprojekt.ui.passport.PassportDataViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.sf.scuba.smartcards.CardService
import org.jmrtd.PassportService
import org.jmrtd.lds.CardAccessFile
import org.jmrtd.lds.PACEInfo
import org.jmrtd.lds.icao.DG1File
import org.jmrtd.lds.icao.DG2File
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.specialeprojekt.data.UserViewModel
import com.example.specialeprojekt.ui.passport.States
import java.io.DataInputStream
import java.io.IOException

class MainActivity : ComponentActivity() {
    private val passportData: PassportDataViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpecialeProjektTheme {
                NavGraph()
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        if (intent.action == NfcAdapter.ACTION_TAG_DISCOVERED) {
            val tag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra(NfcAdapter.EXTRA_TAG, Tag::class.java)
            } else {
                @Suppress("DEPRECATION")
                intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)
            }
            tag?.let { readPassport(it) }
        }
    }

    private fun readPassport(tag: Tag) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val isoDep = IsoDep.get(tag)
                isoDep.connect()
                isoDep.timeout = 15000
                Log.d("PASSPORT", "Connected to tag")
                passportData.failed = false
                passportData.tagConnected = true

                val cardService = CardService.getInstance(isoDep)
                cardService.open()

                val service = PassportService(
                    cardService,
                    PassportService.NORMAL_MAX_TRANCEIVE_LENGTH,
                    PassportService.DEFAULT_MAX_BLOCKSIZE,
                    false, false
                )
                service.open()

                try {
                    val cardAccessFile =
                        CardAccessFile(service.getInputStream(PassportService.EF_CARD_ACCESS))
                    val paceInfo = cardAccessFile.securityInfos
                        .filterIsInstance<PACEInfo>()
                        .firstOrNull()
                    if (paceInfo != null) {
                        @Suppress("DEPRECATION")
                        service.doPACE(passportData.bacKey, paceInfo.objectIdentifier, PACEInfo.toParameterSpec(paceInfo.parameterId))
                        Log.d("PASSPORT", "PACE done")
                    } else {
                        Log.e("PASSPORT", "No PACE info found")
                        passportData.tagConnected = false
                        return@launch
                    }
                } catch (e2: Exception) {
                    Log.e("PASSPORT", "PACE also failed: ${e2.message}", e2)
                    passportData.startNFC = true
                    passportData.tagConnected = false
                    passportData.currentState = States.NFC
                    passportData.failed = true
                    return@launch
                }


                service.sendSelectApplet(true)
                Log.d("PASSPORT", "Applet selected")

                // Read DG1 (MRZ)
                val dg1 = DG1File(service.getInputStream(PassportService.EF_DG1))
                val mrz = dg1.mrzInfo
                Log.d("PASSPORT", "Name: ${mrz.primaryIdentifier}")
                passportData.mrzInfo = mrz
                passportData.tagConnected = false

                // Read DG2 (Photo)
                try {
                    val dg2 = DG2File(service.getInputStream(PassportService.EF_DG2))
                    val faceImageInfo = dg2.faceInfos
                        .firstOrNull()
                        ?.faceImageInfos
                        ?.firstOrNull()

                    if (faceImageInfo != null) {
                        // Extract raw image bytes
                        val imageBytes = ByteArray(faceImageInfo.imageLength)
                        DataInputStream(faceImageInfo.imageInputStream).readFully(imageBytes)

                        // Decode to Bitmap
                        val bitmap: Bitmap? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                            try {
                                val source = android.graphics.ImageDecoder.createSource(
                                    java.nio.ByteBuffer.wrap(imageBytes)
                                )
                                android.graphics.ImageDecoder.decodeBitmap(source)
                            } catch (e: Exception) {
                                Log.e("PASSPORT", "ImageDecoder failed: ${e.message}")
                                BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                            }
                        } else {
                            BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                        }

                        bitmap?.let {
                            userViewModel.passportPhoto = it
                            userViewModel.showPassportImage = true
                        }
                        Log.d("PASSPORT", "Photo loaded: ${bitmap?.width}x${bitmap?.height}")
                    }
                } catch (e: IOException) {
                    passportData.mrzInfo = null;
                    passportData.tagConnected = false;
                }


            } catch (e: Exception) {
                Log.e("PASSPORT", "Failed at: ${e.message}", e)
            }
        }
    }

    @Preview(showBackground = true, showSystemUi = true)
    @Composable
    fun Prev() {
        NavGraph()
    }
}