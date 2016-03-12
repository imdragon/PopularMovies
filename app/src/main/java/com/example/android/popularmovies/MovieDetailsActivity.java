package com.example.android.popularmovies;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
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
        mTitle.setShadowLayer(25, 0, 0, Color.BLACK);

        ImageView mBackdrop = (ImageView) findViewById(R.id.backdropImageView);
        Picasso.with(this).load("http://image.tmdb.org/t/p/w780/"+details.getBackdrop()).into(mBackdrop);
        Log.e("backdrop url", "http://image.tmdb.org/t/p/w780/" + details.getBackdrop());
        ImageView mPoster = (ImageView) findViewById(R.id.mPoster);
        Picasso.with(this).load("http://image.tmdb.org/t/p/w780/" + details.getPoster())
                .placeholder(R.drawable.comingsoon).into(mPoster);
        TextView mOverview = (TextView) findViewById(R.id.synopsis);
        mOverview.setText(details.getSynopsis());
        TextView mRelease = (TextView) findViewById(R.id.releaseDate);
//        mRelease.setText(details.getReleaseDate());
        mRelease.setText("Released: "+details.getReleaseDate().substring(0,4));
        TextView mRating = (TextView) findViewById(R.id.ratingDetail);

        RatingBar mRatingBar = (RatingBar) findViewById(R.id.ratingBar);
        mRatingBar.setRating(Float.valueOf(details.getRating()));
        mRating.setText("Rating: "+details.getRating()+"/10");


    }
}