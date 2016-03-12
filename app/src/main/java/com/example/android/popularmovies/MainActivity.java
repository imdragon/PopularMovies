package com.example.android.popularmovies;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<String> moviePosterAddress = new ArrayList<>();
    private JSONObject popArray;
    private JSONArray movies;
    private ArrayList<Movie> movieObjectArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            new RequestPopularMovies().execute("popularity.desc", null, null);
            Log.e("WAS NULL", "WAS NULL");
        } else {
            moviePosterAddress = savedInstanceState.getStringArrayList("posters");
            movieObjectArray = savedInstanceState.getParcelableArrayList("movies");
            setupGrid();
            Log.e("NOT NULL", "NOT NULL");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.sortChoice) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle(R.string.sort_option).setItems(R.array.sortOptionArray, new DialogInterface.OnClickListener() {
                //// TODO: 3/8/2016 See about styling the AlertDialog without a new layout
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == 0) {
                        new RequestPopularMovies().execute("popularity.desc", null, null);
                        // popularity.desc
                    } else {
                        // below request shows by highest rating for US movies
                        new RequestPopularMovies().execute("certification_country=US&sort_by=vote_average.desc&vote_count.gte=1000", null, null);
                        // rating.desc
                    }
                }
            });
            AlertDialog pop = builder.create();
            pop.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.popup_menu_layout, menu);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.e("SAVEDiN", "SAVEDiN");
        outState.putStringArrayList("posters", moviePosterAddress);
        outState.putParcelableArrayList("movies", movieObjectArray);
        super.onSaveInstanceState(outState);
    }


    private class RequestPopularMovies extends AsyncTask<String, Void, Void> {
        StringBuilder total = new StringBuilder();

        final String MOVIE_BLOCKS = "results";
        final String MOVIE_TITLE = "original_title";
        final String MOVIE_POSTER = "poster_path";
        final String MOVIE_BACKDROP = "backdrop_path";
        final String MOVIE_OVERVIEW = "overview";
        final String MOVIE_RATING = "vote_average";
        final String MOVIE_RELEASE_DATE = "release_date";
        final String MOVIE_RUNTIME = "runtime";

        @Override
        protected Void doInBackground(String... params) {
            // using the apikey in a separate string file to protect the apikey
            try {
                URL url = new URL("https://api.themoviedb.org/3/discover/movie?sort_by=" + params[0] + "&api_key=" + getResources().getString(R.string.apiKey));
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
                // checking if getting back something
                // REMOVE FOR FINAL!
                Log.e("Response", total.toString());
//// TODO: 3/8/2016 Clean up this section
                //JSON section
                popArray = new JSONObject(total.toString());
                movies = popArray.getJSONArray(MOVIE_BLOCKS);
                // REMOVE FOR FINAL!
                Log.e("movie titles", movies.toString());
                moviePosterAddress.clear();
                movieObjectArray = new ArrayList<>();
                // get movie poster filenames and put in an array
                for (int i = 0; i < movies.length(); i++) {
                    Movie tempMovie = new Movie();
                    JSONObject aTitle = movies.getJSONObject(i);
                    moviePosterAddress.add(aTitle.getString(MOVIE_POSTER));
                    // check to see if working
                    Log.e("poster path" + i, aTitle.getString(MOVIE_POSTER));
                    // Build movie object
                    tempMovie.setTitle(aTitle.getString(MOVIE_TITLE));
                    tempMovie.setPoster(aTitle.getString(MOVIE_POSTER));
                    tempMovie.setSynopsis(aTitle.getString(MOVIE_OVERVIEW));
                    tempMovie.setRating(aTitle.getString(MOVIE_RATING));
                    tempMovie.setReleaseDate(aTitle.getString(MOVIE_RELEASE_DATE));
                    tempMovie.setBackdrop(aTitle.getString(MOVIE_BACKDROP));
                    movieObjectArray.add(tempMovie);
                }
                connection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            setupGrid();
        }
    }

    private void setupGrid() {
        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(MainActivity.this, moviePosterAddress));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Movie movieDetails = movieObjectArray.get(position);
                Intent i = new Intent(MainActivity.this, MovieDetailsActivity.class);
                i.putExtra("movieInfo", movieDetails);
                startActivity(i);
            }
        });
    }
}
