package com.spiffynet.abqheadlines.wearos.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import com.spiffynet.abqheadlines.wearos.R

class FrontDisplay {
    @Composable
    fun Front_Page() {
        var goToNews by remember { mutableStateOf(false) }
        var whichSite by remember { mutableStateOf("") }
        TimeText(timeTextStyle = TextStyle(MaterialTheme.colors.primary))
        if (goToNews) {
            NewsDisplay().WearApp(whichSite = whichSite)
        } else {
            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.size(24.dp))
                    Card(
                        border = BorderStroke(1.dp, MaterialTheme.colors.primary),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Transparent
                        )
                    ) {
                        Text(
                            "  ABQHeadlines  ",
                            style = TextStyle(
                                color = MaterialTheme.colors.primary,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                    Spacer(modifier = Modifier.size(12.dp))
                    Image(
                        modifier = Modifier
                            .size(45.dp)
                            .clickable {
                                goToNews = true
                                whichSite = "KRQE"
                            },
                        painter = painterResource(id = R.drawable.krqe_logo_round),
                        contentDescription = null,
                        alignment = Alignment.Center
                    )
                    Spacer(modifier = Modifier.size(5.dp))
                    Image(
                        modifier = Modifier
                            .size(45.dp)
                            .clickable {
                                goToNews = true
                                whichSite = "KOAT"
                            },
                        painter = painterResource(id = R.drawable.koat_logo_round),
                        contentDescription = null,
                        alignment = Alignment.Center
                    )
                    Spacer(modifier = Modifier.size(5.dp))
                    Image(
                        modifier = Modifier
                            .size(45.dp)
                            .clickable {
                                goToNews = true
                                whichSite = "KOB"
                            },
                        painter = painterResource(id = R.drawable.kob_logo_round),
                        contentDescription = null,
                        alignment = Alignment.Center
                    )
                }
            }
        }
    }
}