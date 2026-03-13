package com.example.specialeprojekt.ui.home

import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleLeft
import androidx.compose.material.icons.filled.ArrowCircleRight
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.specialeprojekt.R
import com.example.specialeprojekt.data.AldersBevis
import com.example.specialeprojekt.data.SundhedsKort
import com.example.specialeprojekt.data.UserViewModel
import com.example.specialeprojekt.ui.attestations.AttestationCard
import com.example.specialeprojekt.ui.mitid.MitIDRequestViewModel
import com.example.specialeprojekt.ui.navigation.Route
import kotlinx.coroutines.launch

@Composable
fun MainPage(navController: NavController) {
    val context = LocalContext.current
    val userModel: UserViewModel = viewModel(context as ComponentActivity)
    val attestations = userModel.attestations.values.toList()
    val pagerState = rememberPagerState(pageCount = { attestations.size })
    val scope = rememberCoroutineScope()
    var showAddProofOptions by remember { mutableStateOf(false) }
    var showProofOptions by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        MainPageHeader({ if (attestations.isNotEmpty()) showAddProofOptions = true }, { showProofOptions = true })
        Image(
            painter = painterResource(R.drawable.userpic),
            contentDescription = "user image",
            modifier = Modifier.height(100.dp).width(100.dp)
        )
        Spacer(modifier = Modifier.height(32.dp))
        Text(text = userModel.username)
        Spacer(modifier = Modifier.height(24.dp))


        if (attestations.isEmpty()) {
            // empty state
            Column(
                modifier = Modifier.fillMaxWidth().weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Ingen beviser tilføjet")
                Spacer(modifier = Modifier.height(16.dp))
                ProofButtons(onAddAttestation = { showAddProofOptions = true }, navController)
            }
        } else {
            // carousel
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 60.dp),
                pageSpacing = 5.dp
            ) { page ->
                AttestationCard(attestations[page], navController)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // arrows + name
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowCircleLeft,
                    contentDescription = "Previous",
                    tint = if (pagerState.currentPage == 0) Color.Gray else Color.Black,
                    modifier = Modifier.clickable {
                        scope.launch {
                            if (pagerState.currentPage > 0)
                                pagerState.animateScrollToPage(pagerState.currentPage - 1)
                        }
                    })

                Button(onClick = { navController.navigate(Route.Attestation.route) }) {
                    Text(attestations[pagerState.currentPage].attestationType)
                }

                Icon(
                    imageVector = Icons.Filled.ArrowCircleRight,
                    contentDescription = "Next",
                    tint = if (pagerState.currentPage == pagerState.pageCount -1) Color.Gray else Color.Black,
                    modifier = Modifier.clickable {
                        scope.launch {
                            if (pagerState.currentPage < pagerState.pageCount - 1)
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    })
            }


            Spacer(modifier = Modifier.height(16.dp))
            ProofButtons(onAddAttestation = { showAddProofOptions = true }, navController)
        }
    }

    // dialog outside the column
    if (showAddProofOptions) {
        AddProofAlertBox { showAddProofOptions = false }
    }

}

@Composable
fun AddProofAlertBox(onDismiss: () -> Unit) {
    val context = LocalContext.current
    val userModel: UserViewModel = viewModel(context as ComponentActivity)
    val attestations = userModel.attestations.values.toList()

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Vælg bevis") },
        text = {
            Column {
                listOf(
                    "Aldersbevis" to Icons.Filled.Cake,
                    "Sundhedskort" to Icons.Filled.CreditCard
                ).filter { (name, _) ->
                    attestations.none { it.attestationType == name }
                }.forEach { (name, icon) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                // add only the clicked one
                                when (name) {
                                    "Aldersbevis" -> userModel.addAttestation(AldersBevis())
                                    "Sundhedskort" -> userModel.addAttestation(SundhedsKort())
                                }
                                onDismiss()
                            }
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = icon, contentDescription = null)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(name)
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = { onDismiss() }) {
                Text("Annuller")
            }
        }
    )
}




@Composable
fun ProofButtons(onAddAttestation: () -> Unit, navController: NavController) {
    val context = LocalContext.current
    val mitIDRequestViewModel: MitIDRequestViewModel = viewModel(context as ComponentActivity)
    val userModel: UserViewModel = viewModel(context)

    Row {
        if (userModel.attestations.isEmpty()) {
            Button(onClick = {
                mitIDRequestViewModel.path =  Route.Main.route
                mitIDRequestViewModel.message = "Legitimationsbevis"
                navController.navigate(Route.MitIDAuth.route)
            }) {

                Text("Tilføj legitimationsbevis")
            }
        }
        else {
            Button(onClick = { onAddAttestation() }) {
                Text("Tilføj bevis")
            }
            Button(onClick = { navController.navigate(Route.QRScanner.route) }) {
                Text("Scan QR kode")
            }
        }

    }
}

