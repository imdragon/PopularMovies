package com.example.android.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class MovieDetailsActivity extends AppCompatActivity {
private Bundle incoming = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_view);
Movie details = (Movie) getIntent().getParcelableExtra("movieInfo");
Log.e("movie details", details.getTitle());


        TextView mTitle = (TextView) findViewById(R.id.original_title_detail);

        mTitle.setText(details.getTitle());


    }
}