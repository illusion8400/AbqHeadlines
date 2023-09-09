package com.spiffynet.abqheadlines;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewsItem selectedItem = newsItems.get(position);

                // Check if the link is not empty
                if (!selectedItem.getLink().isEmpty()) {
                    // Open the default browser with the link URL
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(selectedItem.getLink()));
                    startActivity(browserIntent);
                } else {
                    Toast.makeText(MainActivity.this, "No link available", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Set up SwipeRefreshLayout for refreshing
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                new FetchNewsTask().execute();
                finish();
                startActivity(getIntent());
                Toast.makeText(MainActivity.this, "Refresh", Toast.LENGTH_SHORT).show();
            }
        });

        // Start the web scraping task
        new FetchNewsTask().execute();

        // Set a click listener for the ListView items
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewsItem selectedItem = newsItems.get(position);
                // Check if the link is not empty
                if (!selectedItem.getLink().isEmpty()) {
                    // Open the default browser with the link URL
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(selectedItem.getLink()));
                    startActivity(browserIntent);
                } else {
                    Toast.makeText(MainActivity.this, "No link available", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private class FetchNewsTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            // get chosen site
            int tappedImageId = getIntent().getIntExtra("tapped_image_id", -1);
            // KRQE
            if (tappedImageId == R.id.krqe_img) {
                newsItems.add(new NewsItem("KRQE: ", "http://www.krqe.com"));
                try {
                    Document krqeDoc = Jsoup.connect("http://www.krqe.com").get();
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
                newsItems.add(new NewsItem("KOAT: ", "http://www.koat.com"));
                try {
                    Document koatDoc = Jsoup.connect("http://www.koat.com").get();
                    Elements koatElements = koatDoc.select("h2");

                    for (Element element : koatElements) {
                        String title = element.text().trim();
                        String link = element.select("a").attr("href");
                        newsItems.add(new NewsItem(title, "http://www.koat.com" + link));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // KOB
            if (tappedImageId == R.id.kob_img) {
                newsItems.add(new NewsItem("KOB: ", "http://www.kob.com"));
                try {
                    Document kobDoc = Jsoup.connect("http://www.kob.com").get();

                    Elements kobElements = kobDoc.select("h6");
//                kobElements = kobDoc.select("h4");

                    for (Element element : kobElements) {
                        String title = element.text().trim();
                        String link = element.select("a").attr("href");
                        newsItems.add(new NewsItem(title, link));
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

                    return view;
                }
            };
            listView.setAdapter(adapter);
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private class NewsItem {
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
