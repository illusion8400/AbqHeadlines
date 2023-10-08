/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package com.spiffynet.abqheadlines.wearos.presentation

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.rotary.onRotaryScrollEvent
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
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
        setContent {
            WearApp()
        }
    }
}



@Composable
fun WearApp() {
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
                        .focusable()) {
                    Text(
                        "\n\n               ABQHeadlines",
                        textAlign = TextAlign.Center
                    )
                    Divider(color = Color.Red, thickness = 3.dp)
                    for (item in results) {
                        val title = item["title"] ?: ""
                        val link = item["link"] ?: ""

                        ClickableText(
                            text = AnnotatedString(title),
                            onClick = {
                                // Open the link when the title is clicked
                                openLinkInBrowser(link, launcher)
                            },
                            style = TextStyle(color = MaterialTheme.colors.primary),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Divider(color = Color.Blue, thickness = 1.dp)
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
            // Handle the case where a browser app is not installed
        }
    }


