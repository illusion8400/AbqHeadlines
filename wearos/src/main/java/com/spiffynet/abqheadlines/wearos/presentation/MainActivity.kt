package com.spiffynet.abqheadlines.wearos.presentation

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.rotary.onRotaryScrollEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.wear.compose.foundation.ExperimentalWearFoundationApi
import androidx.wear.compose.foundation.rememberActiveFocusRequester
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.spiffynet.abqheadlines.wearos.R
import com.spiffynet.abqheadlines.wearos.presentation.theme.AbqHeadlinesTheme
import kotlinx.coroutines.NonCancellable.key
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        setTheme(android.R.style.Theme_DeviceDefault)
        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        // Launch main app
        setContent {
            WearApp()
        }
    }
}

@OptIn(ExperimentalWearFoundationApi::class)
@Composable
fun WearApp() {

    var refreshing by remember { mutableStateOf(false) }
    // refresh
    LaunchedEffect(refreshing) {
        if (refreshing) {
            delay(3000)
            refreshing = false
        }
    }
    val listState = rememberScrollState()
    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = refreshing),
        onRefresh = { refreshing = true },
    ) {
        Toast.makeText(LocalContext.current, "Loading...", Toast.LENGTH_LONG).show()
        if (!refreshing) {
            Scaffold(
                modifier = Modifier
                    .fillMaxSize(),
                positionIndicator = { PositionIndicator(scrollState = listState) }
            ) {
                val TAG = "WearApp"
                Log.i(TAG, "start")

                val launcher =
                    rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                        if (result.resultCode == Activity.RESULT_OK) {
                            // Handle the result, if needed
                        }
                    }
                // fetch news
                var results by rememberSaveable { mutableStateOf(NewsActivity().fetchNews()) }
                if (refreshing) {
                    results = NewsActivity().fetchNews()
                }

                AbqHeadlinesTheme {

                    // Time at top
                    TimeText(
                        timeTextStyle = TextStyle(MaterialTheme.colors.primary)
                    )

                    // scrolling
                    val flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior()
                    val coroutineScope = rememberCoroutineScope()
                    val focusRequester: FocusRequester = rememberActiveFocusRequester()
                    var selectedLink by remember { mutableStateOf<String?>(null) }
                    var showPageParser by remember { mutableStateOf(false) }
                    LaunchedEffect(listState) { focusRequester.requestFocus() }


                    Column(
                        modifier = Modifier
                            .verticalScroll(listState, flingBehavior = flingBehavior)
                            .onRotaryScrollEvent {
                                // handle rotary scroll events
                                coroutineScope.launch {
                                    listState.scrollBy(it.verticalScrollPixels * 20)
                                    listState.animateScrollBy(0f)
                                }
                                true
                            }
                            .focusRequester(focusRequester)
                            .focusable(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        if (!showPageParser) {
                            Text("")
                            // Title
                            Card(
                                modifier = Modifier.padding(1.dp),
                                border = BorderStroke(1.dp, MaterialTheme.colors.primary),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.Transparent
                                ),
                            ) {
                                Text(
                                    "  ABQHeadlines  ",
                                    style = TextStyle(
                                        color = MaterialTheme.colors.primary,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }


                            // pull results

                            for (item in results) {
                                val title = item["title"] ?: ""
                                val link = item["link"] ?: ""
                                // indent and center
                                val indentedTitle = buildAnnotatedString {
                                    withStyle(
                                        style = ParagraphStyle(
                                            textIndent = TextIndent(firstLine = 5.sp),
                                            textAlign = TextAlign.Center
                                        )
                                    ) {
                                        append(title)
                                    }
                                }
                                // icon and styling
                                when (title) {
                                    "KRQE" -> {
                                        Card(
                                            border = BorderStroke(1.dp, Color.Blue),
                                            colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                                        ) {
                                            Image(
                                                painter = painterResource(id = R.drawable.krqe_logo_round),
                                                contentDescription = null,
                                                alignment = Alignment.Center
                                            )
                                        }
                                        continue
                                    }

                                    "KOAT" -> {
                                        Card(
                                            border = BorderStroke(1.dp, Color.Green),
                                            colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                                        ) {
                                            Image(
                                                painter = painterResource(id = R.drawable.koat_logo_round),
                                                contentDescription = null,
                                                alignment = Alignment.Center
                                            )
                                        }
                                        continue
                                    }

                                    "KOB" -> {
                                        Card(
                                            border = BorderStroke(1.dp, Color.Red),
                                            colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                                        ) {
                                            Image(
                                                painter = painterResource(id = R.drawable.kob_logo_round),
                                                contentDescription = null,
                                                alignment = Alignment.Center
                                            )
                                        }
                                        continue
                                    }
                                }
                                Card(
                                    border =
                                    if (link.contains("krqe")) {
                                        BorderStroke(1.dp, Color.Blue)
                                    } else if (link.contains("koat")) {
                                        BorderStroke(1.dp, Color.Green)
                                    } else if (link.contains("kob")) {
                                        BorderStroke(1.dp, Color.Red)
                                    } else {
                                        BorderStroke(1.dp, Color.Magenta)
                                    },

                                    // background colors
                                    colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                                ) {
                                    ClickableText(
                                        text = indentedTitle,
                                        onClick = {
                                            // Open the link when the title is clicked
//                                        openLinkInBrowser(link, launcher)
                                            selectedLink = link
                                            showPageParser = true

                                        },
                                        // text colors
                                        style = TextStyle(color = MaterialTheme.colors.primary),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                    )
                                }
                            }
                            Text("")
                            Text("")
                        } else {
                            Text(text = "")
                            Image(painter = painterResource(id = R.drawable.app_icon_round),
                                contentDescription = null,
                                modifier = Modifier
                                    .clickable { showPageParser = false }
                            )
                            Toast.makeText(LocalContext.current, "Loading...", Toast.LENGTH_SHORT).show()
                            LaunchedEffect(key1 = key) {listState.scrollTo(0)}
                            selectedLink?.let { link ->
                                PageParser().NewWear(url = link)
                            }
                        }
                    }
                }
            }
        }
    }
}

 fun openLinkInBrowser(link: String, launcher: ActivityResultLauncher<Intent>) {
    val uri = Uri.parse(link)
    val intent = Intent(Intent.ACTION_VIEW, uri)

    try {
        launcher.launch(intent)
    } catch (e: ActivityNotFoundException) {
        // FIXME: Send link to phone or copy link with no browser
        Log.e("NoBrowser", "browser error", e)
    }
}
