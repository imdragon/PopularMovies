package com.example.android.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_view);
        Movie details = getIntent().getParcelableExtra("movieInfo");


        TextView mTitle = (TextView) findViewById(R.id.original_title_detail);
        mTitle.setText(details.getTitle());
        ImageView mPoster = (ImageView) findViewById(R.id.mPoster);
        Picasso.with(this).load("http://image.tmdb.org/t/p/w780/" + details.getPoster())
                .placeholder(R.drawable.comingsoon).into(mPoster);
        TextView mOverview = (TextView) findViewById(R.id.synopsis);
        mOverview.setText(details.getSynopsis());
        TextView mRelease = (TextView) findViewById(R.id.releaseDate);
        mRelease.setText(details.getReleaseDate());
        TextView mRating = (TextView) findViewById(R.id.ratingDetail);
        mRating.setText(details.getRating()+"/10");


    }
}