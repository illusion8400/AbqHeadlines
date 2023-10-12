package com.spiffynet.abqheadlines.wearos.presentation

import android.util.Log
import androidx.activity.ComponentActivity
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.io.IOException

class NewsActivity : ComponentActivity() {
    private val TAG = "NewsActivity"

    fun fetchNews(): ArrayList<Map<String, String>> {
        val results = ArrayList<Map<String, String>>()
        // KRQE
        val krqeUrl = "http://www.krqe.com"
        Log.i(TAG, "Pulled krqe")
        try {
            val krqeDoc: Document = Jsoup.connect(krqeUrl).get()
            val krqeTitles: List<Element> =
                krqeDoc.select("h3.article-list__article-title")
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
        }

        // KOAT
        val koatUrl = "http://www.koat.com"
        Log.i(TAG, "Pulled koat")
        try {
            val koatDoc: Document = Jsoup.connect(koatUrl).get()
            val koatTitles: List<Element> = koatDoc.select("h2, body > div.site-content > main > " +
                    "div.listing-page > div > div.grid-content.listbox > div.grid-content-inner > ul > li > a")
            for (element in koatTitles) {
                val ele1 = element.toString()
                if (ele1.contains("No data available")
                    || ele1.contains("Advertisement")
                    || ele1.contains("Sponsored")
                    || ele1.contains("Promotions")
                    || ele1.contains("Top Picks")
                    || ele1.contains("Good Housekeeping")
                    || ele1.contains("DISH subscribers")
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
        }
        // KOB
        val kobUrl = "http://www.kob.com"
        Log.i(TAG, "Pulled kob")
        try {
            val kobDoc: Document = Jsoup.connect(kobUrl).get()
            val kobTitles: List<Element> = kobDoc.select("div.col-12.col-md-9.col-lg-8.col-xl-8.pb-2,div.col-8," +
                    "div.col-12.col-sm-6.col-md-12.pb-2")
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
        }
        return results
    }
}

