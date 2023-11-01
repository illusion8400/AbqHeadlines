package com.spiffynet.abqheadlines.wearos.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.draw.paint
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.CircularProgressIndicator
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import com.spiffynet.abqheadlines.wearos.R

class FrontDisplay {

    @Composable
    fun FrontPage() {
        var goToNews by remember { mutableStateOf(false) }
        var whichSite by remember { mutableStateOf("") }
        var loading by remember { mutableStateOf(false) }
        var state = remember {
            MutableTransitionState(false).apply {
                targetState = true
            }
        }
        // time for all
        TimeText(timeTextStyle = TextStyle(MaterialTheme.colors.primary))

        // Animate to WearApp when whichSite is changed
        when (goToNews) {
            true -> AnimatedVisibility(visibleState = state) {
                NewsDisplay().WearApp(whichSite = whichSite)
                loading = false
            }
            // show frontPage
            false -> {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .paint(
                            painterResource(id = R.drawable.sandia),
                            contentScale = ContentScale.FillBounds,
                            sizeToIntrinsics = true
                        ),
                ) {
                    TimeText(timeTextStyle = TextStyle(MaterialTheme.colors.primary))
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
                                    color = Color.Black,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                        Spacer(modifier = Modifier.size(12.dp))
                        Image(
                            modifier = Modifier
                                .size(45.dp)
                                .clickable {
                                    loading = true
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
                                    loading = true
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
                                    loading = true
                                    whichSite = "KOB"
                                },
                            painter = painterResource(id = R.drawable.kob_logo_round),
                            contentDescription = null,
                            alignment = Alignment.Center
                        )
                    } // end column
                    if (loading) {
                        CircularProgressIndicator(
                            progress = 1.00f,
                            modifier = Modifier.fillMaxSize(),
                            startAngle = 0f,
                            endAngle = 360f,
                            indicatorColor = Color.Blue,
                            strokeWidth = 4.dp,
                        )
                        AnimatedVisibility(
                            visible = loading,
                            modifier = Modifier.shadow(4.dp),
                        ) {
                            Column(
                                Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Spacer(Modifier.size(32.dp))
                                Box(
                                    modifier = Modifier
                                        .background(Color.Blue)
                                        .border(BorderStroke(1.dp, MaterialTheme.colors.primary)),
                                ) {
                                    Text(text = " Loading... ")
                                }
                            }
                        }
                    }
                } // end scaffold
                if (loading) {
                    goToNews = true
                }
            }
        }
    }
}