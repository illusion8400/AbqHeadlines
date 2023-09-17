package com.spiffynet.abqheadlines;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
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

        // Set up SwipeRefreshLayout for refreshing
        swipeRefreshLayout.setOnRefreshListener(() -> {

            new FetchNewsTask().execute();
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
                    e.printStackTrace();
                }
            }
            // KOAT
            if (tappedImageId == R.id.koat_img) {
                newsItems.add(new NewsItem("KOAT: ", "https://www.koat.com"));
                try {
                    Document koatDoc = Jsoup.connect("https://www.koat.com").get();

                    Elements koatElements = koatDoc.select("h2");
                    for (Element element : koatElements) {
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
                                || ele1.contains("Promotions")) {
                            continue;
                        }
                        String title = element.text().trim();
                        String link = element.select("a").attr("href");
                        newsItems.add(new NewsItem(title, "https://www.koat.com" + link));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
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
                    e.printStackTrace();
                }
            }
            // Alb Journal
            if (tappedImageId == R.id.albj_img) {
                newsItems.add(new NewsItem("Albuquerque Journal: ", "http://www.abqjournal.com/"));
                try {
                    Document kobDoc = Jsoup.connect("http://www.abqjournal.com/").get();
                    Elements kobElements = kobDoc.select("h3");
                    String url = "http://www.abqjournal.com";
                    for (Element element : kobElements) {
                        String title = element.text().trim();
                        String link = element.select("a").attr("href");
                        // links need url added
                        newsItems.add(new NewsItem(title, url + link));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
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
