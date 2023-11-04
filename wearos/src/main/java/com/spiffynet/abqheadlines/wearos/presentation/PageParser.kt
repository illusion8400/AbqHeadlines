package com.spiffynet.abqheadlines.wearos.presentation

import android.content.ContentValues.TAG
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import com.spiffynet.abqheadlines.wearos.presentation.theme.AbqHeadlinesTheme
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException

class PageParser: ComponentActivity() {
    @Composable
    fun NewWear(url: String) {
        val pulledSite = getTheSite(url = url)
        AbqHeadlinesTheme {
            Scaffold{
                    Text(
                        "\n" + pulledSite,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    )
            }
        }
    }
}

    fun getTheSite(url:String): String {
        try {
            val pulledSite: Document = Jsoup.connect(url).get()
            val pulledSite1 =
                when {
                    (url.contains("krqe")) -> {
                        pulledSite
                            .select("main > article > div.article-content.article-body.rich-text > p")
                    }

                    (url.contains("koat")) -> {
                        pulledSite
                            .select(
                                "body > div.site-content > main > div > div.articles-container > " +
                                        "article > div > div.article-content--body > div > " +
                                        "div.article-content--body-inner > div.article-content--body-text > p"
                            )
                    }

                    (url.contains("kob")) -> {
                        pulledSite.select("#storyContent > p")
                    }

                    else -> {
                        pulledSite.select("body")
                    }
                }

            return pulledSite1.text()
        }
        catch (e: IOException) {
            Log.e(TAG, "Error fetching webpage", e)
        }
        catch (e: IllegalArgumentException) {
            Log.e(TAG, "Error fetching webpage", e)
        }
        return "Error fetching webpage"
    }


