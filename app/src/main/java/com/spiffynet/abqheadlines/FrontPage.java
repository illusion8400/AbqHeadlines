package com.spiffynet.abqheadlines;


import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Slide;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class FrontPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.front_page_layout);
    }

    // Handle ImageButton clicks
    public void onImageButtonClick(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        // Slide transition
        getWindow().setExitTransition(new Slide());
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this);
        // setup send tappedImageID to MainActivity through intent
        int tappedImageId = view.getId();
        intent.putExtra("tapped_image_id", tappedImageId);

        startActivity(intent, options.toBundle());
    }
}