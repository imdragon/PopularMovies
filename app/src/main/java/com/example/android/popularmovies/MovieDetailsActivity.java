package com.example.android.popularmovies;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MovieDetailsActivity extends AppCompatActivity {
    Movie details = new Movie();
    JSONArray trailers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_view);
        details = getIntent().getParcelableExtra("movieInfo");


        TextView mTitle = (TextView) findViewById(R.id.original_title_detail);
        mTitle.setText(details.getTitle());
        mTitle.setShadowLayer(25, 0, 0, Color.BLACK);

        ImageView mBackdrop = (ImageView) findViewById(R.id.backdropImageView);
        Picasso.with(this).load("http://image.tmdb.org/t/p/w780/" + details.getBackdrop()).into(mBackdrop);
        Log.e("backdrop url", "http://image.tmdb.org/t/p/w780/" + details.getBackdrop());
        ImageView mPoster = (ImageView) findViewById(R.id.mPoster);
        Picasso.with(this).load("http://image.tmdb.org/t/p/w780/" + details.getPoster())
                .placeholder(R.drawable.comingsoon).into(mPoster);
        TextView mOverview = (TextView) findViewById(R.id.synopsis);
        mOverview.setText(details.getSynopsis());
        TextView mRelease = (TextView) findViewById(R.id.releaseDate);
        mRelease.setText("Released: " + details.getReleaseDate().substring(0, 4));
        TextView mRating = (TextView) findViewById(R.id.ratingDetail);

        RatingBar mRatingBar = (RatingBar) findViewById(R.id.ratingBar);
        mRatingBar.setRating(Float.valueOf(details.getRating()));
        mRating.setText("Rating: " + details.getRating() + "/10");
        new getTrailer().execute(details.getMovieId(), null, null);
    }

    private class getTrailer extends AsyncTask<String, Void, Void> {
        StringBuilder total = new StringBuilder();
        String firstTrailer;

        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p/>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @param params The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */
        @Override
        protected Void doInBackground(String... params) {


            try

            {
                URL url = new URL("http://api.themoviedb.org/3/movie/" + params[0] + "/videos?api_key=" + getResources().getString(R.string.apiKey));
                // making url request and sending it to be read
                Log.e("my url is", String.valueOf(url));
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                // preparing a reader to go through the response
                BufferedReader r = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                // below allows for controlled reading of potentially large text
                String line;
                while ((line = r.readLine()) != null) {
                    total.append(line);
                }
                JSONObject popArray = new JSONObject(total.toString());
                trailers = popArray.getJSONArray("results");
                for (int i = 0; i < trailers.length(); i++) {
                    JSONObject trailer = trailers.getJSONObject(i);
                    firstTrailer = trailer.getString("key");
                }
                Log.e("Trail", total.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            TextView mTrailer = (TextView) findViewById(R.id.trailerLink);
            mTrailer.setText("http://www.youtube.com/watch?v="+firstTrailer);
        }


    }
}
