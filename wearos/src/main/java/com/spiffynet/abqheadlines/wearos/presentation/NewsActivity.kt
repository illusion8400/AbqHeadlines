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
        try {
            val krqeDoc: Document = Jsoup.connect(krqeUrl).get()
            val krqeTitles: List<Element> =
                krqeDoc.select("h3.article-list__article-title")
            for (element in krqeTitles) {
                val title = element.text().trim()
                val link = krqeUrl + element.select("a").attr("href")
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
        try {
            val koatDoc: Document = Jsoup.connect(koatUrl).get()
            val koatTitles: List<Element> = koatDoc.select("h2")
            for (element in koatTitles) {
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

        return results
    }
}

