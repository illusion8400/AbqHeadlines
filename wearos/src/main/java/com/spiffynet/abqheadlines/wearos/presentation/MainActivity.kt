package com.spiffynet.abqheadlines.wearos.presentation

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.rotary.onRotaryScrollEvent
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.spiffynet.abqheadlines.wearos.presentation.theme.AbqHeadlinesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        setTheme(android.R.style.Theme_DeviceDefault)
        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        // Launch main app with swipe refresh
        setContent {
            WearApp()
        }
    }
}

@Composable
fun WearApp() {
    val TAG = "WearApp"
    Log.i(TAG,"start")
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // Handle the result, if needed
            }
        }
    val focusRequester: FocusRequester = remember { FocusRequester() }

    AbqHeadlinesTheme {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.background),
            ) {
                val results = NewsActivity().fetchNews()

                Column(
                    Modifier
                        .verticalScroll(rememberScrollState())
                        .onRotaryScrollEvent {
                            // handle rotary scroll events
                            true
                        }
                        .focusRequester(focusRequester)
                        .focusable() ,
                        horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Title
                    Text("")
                    Card( modifier = Modifier.padding(1.dp),
                        border = BorderStroke(1.dp, Color.Red),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Transparent
                        ),
                    ) {
                        Text("  ABQHeadlines  ")
                    }
                    // pull results
                    Text("")
                    // sort items
                    for (item in results) {
                        val title = item["title"] ?: ""
                        val link = item["link"] ?: ""
                        // indent and center
                        val indentedTitle = buildAnnotatedString {
                            withStyle(
                                style = ParagraphStyle(textIndent = TextIndent(firstLine = 5.sp),
                                    textAlign = TextAlign.Center)
                            ) {
                                append(title)
                            }
                        }
                        Card(
                            // background colors
                            colors = CardDefaults.cardColors(
                                containerColor = Color.Transparent
                            ),
                            border =
                            if (link.contains("krqe")) { BorderStroke(1.dp, Color.Blue) }
                            else if(link.contains("koat")) { BorderStroke(1.dp, Color.Green) }
                            else if(link.contains("kob")) { BorderStroke(1.dp, Color.Red) }
                            else { BorderStroke(1.dp, Color.Magenta)
                            },
                        ) {
                            ClickableText(
                                text = indentedTitle,
                                onClick = {
                                    // Open the link when the title is clicked
                                    openLinkInBrowser(link, launcher)
                                },
                                // text colors
                                style = TextStyle(color = MaterialTheme.colors.primary),
                                modifier = Modifier.fillMaxWidth()
                            )

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