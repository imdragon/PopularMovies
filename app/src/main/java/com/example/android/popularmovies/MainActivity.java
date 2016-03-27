package com.example.android.popularmovies;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.Toast;

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
    GridView gridview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gridview = (GridView) findViewById(R.id.gridview);

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
                    }
                    if (which == 1) {
                        // below request shows by highest rating for US movies
                        new RequestPopularMovies().execute("certification_country=US&sort_by=vote_average.desc&vote_count.gte=1000", null, null);
                        // rating.desc
                    }
                    if (which == 2) {
                        int newCount = getContentResolver().delete(MovDBContract.MovieEntry.CONTENT_URI, null, null);
                        Toast.makeText(MainActivity.this, String.valueOf(newCount), Toast.LENGTH_SHORT).show();
                        // here to refresh the gridview when delete all
                        favoriteLayout();
                    }
                }
            });
            AlertDialog pop = builder.create();
            pop.show();
        }
        if (item.getItemId() == R.id.Favorites) {
            favoriteLayout();
        }
        return super.onOptionsItemSelected(item);
    }

    public void favoriteLayout() {
        movieObjectArray.clear();
        moviePosterAddress.clear();
        String[] mProjection = {
                MovDBContract.MovieEntry.COLUMN_TITLE,
                MovDBContract.MovieEntry.COLUMN_MOVIEID,
                MovDBContract.MovieEntry.COLUMN_DESCRIPTION,
                MovDBContract.MovieEntry.COLUMN_POSTER,
                MovDBContract.MovieEntry.COLUMN_BACKDROP,
                MovDBContract.MovieEntry.COLUMN_RATING,
                MovDBContract.MovieEntry.COLUMN_RELEASE,
                MovDBContract.MovieEntry.COLUMN_FAVORITE
        };
          /*    0 Title
                1 MovieID
                2 Synopsis
                3 Poster
                4 Backdrop
                5 Rating
                6 Release
                7 favorite <--- probably don't need it */
        Cursor cs = getContentResolver().query(MovDBContract.MovieEntry.CONTENT_URI, mProjection, null, null, null);
        if (cs == null) {
            Log.e("Output:", String.valueOf(cs.getCount()));
        } else {
            while (cs.moveToNext()) {
                moviePosterAddress.add(cs.getString(3));
                Movie tempMovie = new Movie();
                tempMovie.setTitle(cs.getString(0));
                tempMovie.setPoster(cs.getString(3));
                tempMovie.setSynopsis(cs.getString(2));
                tempMovie.setRating(cs.getString(5));
                tempMovie.setReleaseDate(cs.getString(6));
                tempMovie.setBackdrop(cs.getString(4));
                tempMovie.setMovieId(cs.getString(1));
                movieObjectArray.add(tempMovie);
            }
            setupGrid();
        }
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

    private void setupGrid() {
        gridview.setAdapter(new ImageAdapter(MainActivity.this, moviePosterAddress));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Movie movieDetails = movieObjectArray.get(position);
                Log.e("movieID", movieObjectArray.get(position).getMovieId());
                Intent i = new Intent(MainActivity.this, MovieDetailsActivity.class);
                i.putExtra("movieInfo", movieDetails);
                startActivity(i);
            }
        });
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
        final String MOVIE_ID = "id";
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
                //JSON section
                popArray = new JSONObject(total.toString());
                movies = popArray.getJSONArray(MOVIE_BLOCKS);
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
                    tempMovie.setMovieId(aTitle.getString(MOVIE_ID));
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
}


