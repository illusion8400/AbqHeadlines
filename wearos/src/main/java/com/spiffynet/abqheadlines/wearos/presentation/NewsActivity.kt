package com.spiffynet.abqheadlines.wearos.presentation

import android.util.Log
import androidx.activity.ComponentActivity
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.io.IOException

class NewsActivity : ComponentActivity() {
    private val TAG = "NewsActivity"

    @Suppress("NAME_SHADOWING")
    fun fetchNews(whichSite: String): ArrayList<Map<String, String>> {
        val results = ArrayList<Map<String, String>>()
        // KRQE
        if (whichSite == "KRQE") {
            val krqeUrl = "http://www.krqe.com"
            Log.i(TAG, "Pulled krqe")
            try {
                val krqeDoc: Document = Jsoup.connect(krqeUrl).get()
                val krqeTitles: List<Element> =
                    krqeDoc.select("h3.article-list__article-title")
                // add title
                val titleLead = "KRQE"
                val result = HashMap<String, String>()
                result["title"] = titleLead
                result["link"] = krqeUrl
                results.add(result)
                // start
                for (element in krqeTitles) {
                    val title = element.text().trim()
                    val link = element.select("a").attr("href")
                    val result = HashMap<String, String>()
                    result["title"] = title
                    result["link"] = link
                    results.add(result)
                }
            } catch (e: IOException) {
                Log.e(TAG, "Error fetching KRQE news", e)
                val titleLead = "KRQE - NOT AVAILABLE"
                val link = "KRQE - NO LINK"
                val result = HashMap<String, String>()
                result["title"] = titleLead
                result["link"] = link
                results.add(result)
            }
        }
        else if (whichSite == "KOAT") {
            // KOAT
            val koatUrl = "http://www.koat.com"
            Log.i(TAG, "Pulled koat")
            try {
                val koatDoc: Document = Jsoup.connect(koatUrl).get()
                val koatTitles: List<Element> = koatDoc.select(
                    "h2, body > div.site-content > main > " +
                            "div.listing-page > div > div.grid-content.listbox > div.grid-content-inner > ul > li > a"
                )
                // add title
                val titleLead = "KOAT"
                val result = HashMap<String, String>()
                result["title"] = titleLead
                result["link"] = koatUrl
                results.add(result)
                // start
                for (element in koatTitles) {
                    val ele1 = element.toString()
                    if (ele1.contains("No data available")
                        || ele1.contains("Advertisement")
                        || ele1.contains("Sponsored")
                        || ele1.contains("Promotions")
                        || ele1.contains("Top Picks")
                        || ele1.contains("Good Housekeeping")
                        || ele1.contains("DISH subscribers")
                        || ele1.contains("KOAT Albuquerque")
                    ) {
                        continue
                    }
                    val title = element.text().trim()
                    val link = koatUrl + element.select("a").attr("href")
                    val result = HashMap<String, String>()
                    result["title"] = title
                    result["link"] = link
                    results.add(result)
                }
            } catch (e: IOException) {
                Log.e(TAG, "Error fetching KOAT news", e)
                val titleLead = "KOAT - NOT AVAILABLE"
                val link = "KOAT - NO LINK"
                val result = HashMap<String, String>()
                result["title"] = titleLead
                result["link"] = link
                results.add(result)
            }
        }
        else if (whichSite == "KOB") {
            // KOB
            val kobUrl = "http://www.kob.com"
            Log.i(TAG, "Pulled kob")
            try {
                val kobDoc: Document = Jsoup.connect(kobUrl).get()
                val kobTitles: List<Element> = kobDoc.select(
                    "div.col-12.col-md-9.col-lg-8.col-xl-8.pb-2," +
                            "div.col-12.col-sm-6.col-md-12.pb-2," +
                            "#hbi2020-headlines-images-featured-standard-block_4e63aa739cc6847eb9f81dfcaa37103c > div > div > div > div.row.p-0.mx-0 > div > div > div.col-8.col-sm-9.col-md-7.col-lg-8," +
                            "#hbi2020-headlines-must-see-block_d27b46167efacb8cf5843db6514e1288 > div > div > div > div > div > div," +
                            "#hbi2020-headlines-must-see-block_d27b46167efacb8cf5843db6514e1288 > div > div > div > div > div > div," +
                            "#hbi2020-headlines-must-see-block_87ddfeb0d84f772ca82df6def3bf09b1 > div > div > div > div > div > div"
                )
                // add title
                val titleLead = "KOB"
                val result = HashMap<String, String>()
                result["title"] = titleLead
                result["link"] = kobUrl
                results.add(result)
                // start
                for (element in kobTitles) {
                    val title = element.text().trim()
                    val link = element.select("a").attr("href")
                    val result = HashMap<String, String>()
                    result["title"] = title
                    result["link"] = link
                    results.add(result)
                }
            } catch (e: IOException) {
                Log.e(TAG, "Error fetching KOB news", e)
                val titleLead = "KOB - NOT AVAILABLE"
                val link = "KOB - NO LINK"
                val result = HashMap<String, String>()
                result["title"] = titleLead
                result["link"] = link
                results.add(result)
            }
        }
        return results
    }
}

