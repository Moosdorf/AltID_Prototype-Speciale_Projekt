package com.example.specialeprojekt.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.specialeprojekt.R
import com.example.specialeprojekt.data.LegitimationsBevis

@Composable
fun MainPageHeader(legitimationsBevisAdded: Boolean, addProof: () -> Unit, showMenu: () -> Unit) {
    Surface(color = Color(0xFFFFFBFE)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left side
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(R.drawable.digst),
                    contentDescription = "digst logo",
                    modifier = Modifier.height(20.dp).width(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("AltID")
            }

            // Right side
            Row {
                if (legitimationsBevisAdded) {
                    Icon(
                        modifier = Modifier.clickable {
                            addProof()
                        },
                        imageVector = Icons.Filled.AddCircle,
                        contentDescription = "Add",
                        tint = Color.Black
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))
                Icon(
                    modifier = Modifier.clickable {
                        showMenu()
                    },
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Menu",
                    tint = Color.Black
                )
            }
        }
    }
}