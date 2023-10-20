package com.spiffynet.abqheadlines.wearos.presentation

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import com.spiffynet.abqheadlines.wearos.presentation.theme.AbqHeadlinesTheme
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class PageParser: ComponentActivity() {
    @Composable
    fun NewWear(url: String) {
        val pulledSite = getTheSite(url = url)
        AbqHeadlinesTheme {
            Scaffold{
                    Text(
                        "\n" + pulledSite + "\n\n\n\n",
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    )
            }
        }
    }
}

    fun getTheSite(url:String): String {
        val pulledSite: Document = Jsoup.connect(url).get()
        val pulledSite1 =
            pulledSite.select("main > article > div.article-content.article-body.rich-text > p")
        return pulledSite1.text()
    }

