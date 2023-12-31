package com.spiffynet.abqheadlines;


import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.transition.Slide;
import android.view.View;
import android.view.animation.RotateAnimation;

import androidx.appcompat.app.AppCompatActivity;

public class FrontPage extends AppCompatActivity {

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Slide transition
        getWindow().setExitTransition(new Slide());
        getWindow().setReenterTransition(new Slide());
        // tablet or phone orientation
        if (getResources().getBoolean(R.bool.portrait_only)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        }
        setContentView(R.layout.front_page_layout);
        // animate front_text
        RotateAnimation anim = new RotateAnimation(0f, 360f);
        anim.setDuration(1000);
        View tv1= findViewById(R.id.relLayout);
        tv1.startAnimation(anim);

    }

    // Handle ImageButton clicks
    public void onImageButtonClick(View view) {
        Intent intent = new Intent(this, MainActivity.class);

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this);
        // setup send tappedImageID to MainActivity through intent
        int tappedImageId = view.getId();
        intent.putExtra("tapped_image_id", tappedImageId);

        startActivity(intent, options.toBundle());
    }
}