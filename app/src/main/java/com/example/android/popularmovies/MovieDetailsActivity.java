package com.example.android.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;

import java.util.ArrayList;

public class MovieDetailsActivity extends AppCompatActivity {
    Movie details = new Movie();
    String trailerLink;
    Button fButton;
    ArrayList<String> reviews = new ArrayList<String>();
    ArrayAdapter<String> rAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_view);
        details = getIntent().getParcelableExtra("movieInfo");
        rAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, reviews);
        ListView reviewsList = (ListView) findViewById(R.id.reviewsListView);
        reviewsList.setOnTouchListener(new View.OnTouchListener() {
            /**
             * Called when a touch event is dispatched to a view. This allows listeners to
             * get a chance to respond before the target view.
             *
             * @param v     The view the touch event has been dispatched to.
             * @param event The MotionEvent object containing full information about
             *              the event.
             * @return True if the listener has consumed the event, false otherwise.
             */
            @Override

            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        reviewsList.setAdapter(rAdapter);
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

        fButton = (Button) findViewById(R.id.favButton);

        RatingBar mRatingBar = (RatingBar) findViewById(R.id.ratingBar);
        mRatingBar.setRating(Float.valueOf(details.getRating()));
        mRating.setText("Rating: " + details.getRating() + "/10");
        new getTrailerOrReviews(this).execute(0, null, null);
        favoriteCheck();
    }

//    private class getTrailerOrReview extends AsyncTask<String, Void, Void> {
//        StringBuilder total = new StringBuilder();
//        String firstTrailer;
//
//
//        /**
//         * Override this method to perform a computation on a background thread. The
//         * specified parameters are the parameters passed to {@link #execute}
//         * by the caller of this task.
//         * <p/>
//         * This method can call {@link #publishProgress} to publish updates
//         * on the UI thread.
//         *
//         * @param params The parameters of the task.
//         * @return A result, defined by the subclass of this task.
//         * @see #onPreExecute()
//         * @see #onPostExecute
//         * @see #publishProgress
//         */
//        @Override
//        protected Void doInBackground(String... params) {
//            try {
//                URL url = new URL("http://api.themoviedb.org/3/movie/" + params[0] + "/"+params[1]+"?api_key=" + getResources().getString(R.string.apiKey));
//                // making url request and sending it to be read
//                Log.e("my url is", String.valueOf(url));
//                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                // preparing a reader to go through the response
//                BufferedReader r = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//                // below allows for controlled reading of potentially large text
//                String line;
//                while ((line = r.readLine()) != null) {
//                    total.append(line);
//                }
//                JSONObject popArray = new JSONObject(total.toString());
//                trailers = popArray.getJSONArray("results");
//                for (int i = 0; i < trailers.length(); i++) {
//                    JSONObject trailer = trailers.getJSONObject(i);
//                    firstTrailer = trailer.getString("key");
//                    trailerLink = firstTrailer;
//                }
//                Log.e("Trail", total.toString());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            TextView mTrailer = (TextView) findViewById(R.id.trailerLink);
//            mTrailer.setVisibility(View.VISIBLE);
//        }
//    }

    public void addFavorite(View v) {

            ContentValues fav = new ContentValues();

            fav.put(MovDBContract.MovieEntry.COLUMN_MOVIEID, details.getMovieId());
            fav.put(MovDBContract.MovieEntry.COLUMN_TITLE, details.getTitle());
            fav.put(MovDBContract.MovieEntry.COLUMN_DESCRIPTION, details.getSynopsis());
            fav.put(MovDBContract.MovieEntry.COLUMN_POSTER, details.getPoster());
            fav.put(MovDBContract.MovieEntry.COLUMN_BACKDROP, details.getBackdrop());
            fav.put(MovDBContract.MovieEntry.COLUMN_RATING, details.getRating());
            fav.put(MovDBContract.MovieEntry.COLUMN_RELEASE, details.getReleaseDate());
            fav.put(MovDBContract.MovieEntry.COLUMN_FAVORITE, "favorite");

            Uri rUri = getContentResolver().insert(MovDBContract.MovieEntry.CONTENT_URI, fav);
            if (rUri == null) {
                removeFavorite();
            } else {
                Toast.makeText(this, rUri.toString(), Toast.LENGTH_SHORT).show();
                fButton.setBackgroundColor(Color.YELLOW);
                fButton.setText("Favorite!");
            }
    }

    private Boolean favoriteCheck() {
        Boolean flag = false;
        Cursor cs = getContentResolver().query(MovDBContract.MovieEntry.CONTENT_URI, new String[]{MovDBContract.MovieEntry.COLUMN_MOVIEID}, null, null, null);
        if (cs == null) {
            return flag;
        } else {
            while (cs.moveToNext()) {
                if (cs.getString(0).equals(details.getMovieId())) {
                    fButton.setBackgroundColor(Color.YELLOW);
                    fButton.setText("Favorite!");
                    flag = true;
                } else {
                    flag = false;
                }
            }
        }
        return flag;
    }

    private void removeFavorite() {
        getContentResolver().delete(MovDBContract.MovieEntry.CONTENT_URI, MovDBContract.MovieEntry.COLUMN_MOVIEID + "=?", new String[]{details.getMovieId()});
        fButton.setText("Mark as\nfavorite");
        fButton.setBackgroundColor(Color.CYAN);

    }

    public void watchTrailer(View v) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + trailerLink)));
    }
}

