package com.spiffynet.abqheadlines;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/** @noinspection deprecation*/
public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private final List<NewsItem> newsItems = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        // Set up SwipeRefreshLayout for refreshing
        swipeRefreshLayout.setOnRefreshListener(() -> {
            finish();
            startActivity(getIntent());
        });

        // Start the web scraping task
        new FetchNewsTask().execute();
        Toast.makeText(MainActivity.this, "Loading", Toast.LENGTH_SHORT).show();

        // Set a click listener for the ListView items
        listView.setOnItemClickListener((parent, view, position, id) -> {
            NewsItem selectedItem = newsItems.get(position);
            // Check if the link is not empty
            if (!selectedItem.getLink().isEmpty()) {
                // Open the default browser with the link URL
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(selectedItem.getLink()));
                startActivity(browserIntent);
            } else {
                Toast.makeText(MainActivity.this, "No link available", Toast.LENGTH_SHORT).show();
            }
        });
        // Set a long click listener
        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            NewsItem selectedItem = newsItems.get(position);
            // Check if the link is not empty
            if (!selectedItem.getLink().isEmpty()) {
                String textToCopy = String.valueOf(Uri.parse(selectedItem.getLink()));
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // Create a ClipData object to store the text
                ClipData clipData = ClipData.newPlainText("text", textToCopy);
                // Set the data to clipboard
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(MainActivity.this, textToCopy + "\nCopied to clipboard", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "No link available", Toast.LENGTH_SHORT).show();
            }
            return true;
        });
    }

    @SuppressLint("StaticFieldLeak")
    private class FetchNewsTask extends AsyncTask<Void, Void, Void> {
        private static final String TAG = "FetchNewsTask";

        @Override
        protected Void doInBackground(Void... voids) {
            // get chosen site
            int tappedImageId = getIntent().getIntExtra("tapped_image_id", -1);
            // KRQE
            if (tappedImageId == R.id.krqe_img) {
                newsItems.add(new NewsItem("KRQE: ", "https://www.krqe.com"));
                try {
                    Document krqeDoc = Jsoup.connect("https://www.krqe.com").get();
                    Elements krqeElements = krqeDoc.select("h3.article-list__article-title");

                    for (Element element : krqeElements) {
                        String title = element.text().trim();
                        String link = element.select("a").attr("href");
                        newsItems.add(new NewsItem(title, link));
                    }
                } catch (IOException e) {
                    Log.e(TAG, "krqe error", e);
                }
            }
            // KOAT
            if (tappedImageId == R.id.koat_img) {
                newsItems.add(new NewsItem("KOAT: ", "https://www.koat.com"));
                try {
                    Document koatDoc = Jsoup.connect("https://www.koat.com").get();

                    Elements koatElements = koatDoc.select("h2");
                    for (Element element : koatElements) {
                        String ele1 = String.valueOf(element);
                        if (ele1.contains("No data available")
                                || ele1.contains("Advertisement")
                                || ele1.contains("Sponsored")
                                || ele1.contains("Promotions")
                                || ele1.contains("Top Picks")
                                || ele1.contains("Good Housekeeping")
                                || ele1.contains("DISH subscribers")) {
                            continue;
                        }
                        String title = element.text().trim();
                        String link = element.select("a").attr("href");
                        newsItems.add(new NewsItem(title, "https://www.koat.com" + link));
                    }
                    koatElements = koatDoc.select("body > div.site-content > main > " +
                            "div.listing-page > div > div.grid-content.listbox > div.grid-content-inner > ul > li > a");
                    for (Element element : koatElements) {
                        String ele1 = String.valueOf(element);
                        if (ele1.contains("No data available")
                                || ele1.contains("Advertisement")
                                || ele1.contains("Sponsored")
                                || ele1.contains("Promotions")
                                || ele1.contains("Top Picks")
                                || ele1.contains("Good Housekeeping")
                                || ele1.contains("DISH subscribers")) {
                            continue;
                        }
                        String title = element.text().trim();
                        String link = element.select("a").attr("href");
                        newsItems.add(new NewsItem(title, "https://www.koat.com" + link));
                    }
                } catch (IOException e) {
                    Log.e(TAG, "koat error", e);
                }
            }
            // KOB
            if (tappedImageId == R.id.kob_img) {
                newsItems.add(new NewsItem("KOB: ", "https://www.kob.com"));
                try {
                    Document kobDoc = Jsoup.connect("https://www.kob.com").get();
                    Elements kobElements = kobDoc.select("div.col-12.col-md-9.col-lg-8.col-xl-8.pb-2");
                    for (Element element : kobElements) {
                        String title = element.text().trim();
                        String link = element.select("a").attr("href");
                        newsItems.add(new NewsItem(title, link));
                    }
                    kobElements = kobDoc.select("div.col-12.col-sm-6.col-md-12.pb-2");
                    for (Element element : kobElements) {
                        String title = element.text().trim();
                        String link = element.select("a").attr("href");
                        newsItems.add(new NewsItem(title, link));
                    }
                    kobElements = kobDoc.select("div.col-8");
                    for (Element element : kobElements) {
                        String title = element.text().trim();
                        String link = element.select("a").attr("href");
                        newsItems.add(new NewsItem(title, link));
                    }
                } catch (IOException e) {
                    Log.e(TAG, "kob error", e);
                }
            }
            // Alb Journal
            if (tappedImageId == R.id.albj_img) {
                newsItems.add(new NewsItem("Albuquerque Journal: ", "https://www.abqjournal.com/"));
                try {
                    Document albjDoc = Jsoup.connect("https://www.abqjournal.com/").get();
                    Elements albjElements = albjDoc.select("h3");
                    String url = "https://www.abqjournal.com";
                    for (Element element : albjElements) {
                        String title = element.text().trim();
                        String link = element.select("a").attr("href");
                        // links need url added
                        newsItems.add(new NewsItem(title, url + link));
                    }
                } catch (IOException e) {
                    Log.e(TAG, "abqj error", e);
                }
            }
            // Sante Fe New Mexican
            if (tappedImageId == R.id.sfnm_img) {
                newsItems.add(new NewsItem("Santa Fe New Mexican: ", "https://www.santafenewmexican.com/"));
                try {
                    Document sfnmDoc = Jsoup.connect("https://www.santafenewmexican.com/").get();
                    Elements sfnmElements = sfnmDoc.select("div.card-container > div.card-body > div.card-headline");
                    String url = "https://www.santafenewmexican.com";
                    for (Element element : sfnmElements) {
                        String title = element.text().trim();
                        String link = element.select("a").attr("href");
                        // links need url added
                        newsItems.add(new NewsItem(title, url + link));
                    }
                } catch (IOException e) {
                    Log.e(TAG, "sfnm error", e);
                }
            }
            // SourceNM
            if (tappedImageId == R.id.sourcenm_img) {
                newsItems.add(new NewsItem("SourceNM: ", "https://sourcenm.com/"));
                try {
                    Document sourcenmDoc = Jsoup.connect("https://sourcenm.com/").get();
                    Elements sourcenmElements = sourcenmDoc.select("h3");
                    for (Element element : sourcenmElements) {
                        String ele1 = String.valueOf(element);
                        if (ele1.contains("Crisis on the Rio Grande")
                                || ele1.contains("Fuente")) {
                            continue;
                        }
                        String title = element.text().trim();
                        String link = element.select("a").attr("href");
                        // links need url added
                        newsItems.add(new NewsItem(title, link));
                    }
                    sourcenmElements = sourcenmDoc.select("h4");
                    String url = "https://sourcenm.com/subscribe/";
                    for (Element element : sourcenmElements) {
                        String title = element.text().trim();
                        String link = element.select("a").attr("href");
                        if (title.contains("ABOUT US")) {
                            link = url;
                        }
                        newsItems.add(new NewsItem(title, link));
                    }
                    String url2 = "https://sourcenm.com/donate/";
                    newsItems.add(new NewsItem("Donate", url2));
                } catch (IOException e) {
                    Log.e(TAG, "sourcenm error", e);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            ArrayAdapter<NewsItem> adapter = new ArrayAdapter<NewsItem>(
                    MainActivity.this,
                    R.layout.list_item_layout,
                    R.id.item_title,
                    newsItems) {
                @NonNull
                @Override
                public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);

                    TextView titleTextView = view.findViewById(R.id.item_title);
                    TextView linkTextView = view.findViewById(R.id.item_link);

                    NewsItem newsItem = getItem(position);

                    if (newsItem != null) {
                        titleTextView.setText(newsItem.getTitle());
                        linkTextView.setText(newsItem.getLink());
                    }
                    // Add Colors
                    if (titleTextView.getText().toString().contains("KRQE")) {
                        titleTextView.setTextColor(getResources().getColor(R.color.blue));
                        linkTextView.setTextColor(getResources().getColor(R.color.blue));
                    }  else if (titleTextView.getText().toString().contains("KOAT")) {
                        titleTextView.setTextColor(getResources().getColor(R.color.blue));
                        linkTextView.setTextColor(getResources().getColor(R.color.blue));
                    } else if (titleTextView.getText().toString().contains("KOB")) {
                        titleTextView.setTextColor(getResources().getColor(R.color.red));
                        linkTextView.setTextColor(getResources().getColor(R.color.red));
                    } else {
                        // default if not found
                        titleTextView.setTextColor(getResources().getColor(R.color.text_color_light_or_dark));
                        linkTextView.setTextColor(getResources().getColor(R.color.grey));
                    }
                    return view;
                }
            };
            listView.setAdapter(adapter);
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private static class NewsItem {
        private final String title;
        private final String link;

        public NewsItem(String title, String link) {
            this.title = title;
            this.link = link;
        }

        public String getTitle() {
            return title;
        }

        public String getLink() {
            return link;
        }
    }

}
