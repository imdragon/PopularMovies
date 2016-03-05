package com.example.android.popularmovies;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {
    public Context mContext;
    private ArrayList<String> moviePosterAddress = new ArrayList<>();
    private JSONObject popArray;
    private JSONArray movies;
    final String MOVIE_POSTER = "poster_path";
    private String[] moviePathString = new String[30];
    Boolean flag = false;

    public ImageAdapter(Context c) {
        new RequestPopularMovies().execute(null, null, null);
        mContext = c;
        mContext.getResources().getStringArray();
    }

    @Override
    public int getCount() {
        return moviePathString.length;
    }

    @Override
    public Object getItem(int position) {
        return moviePathString[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
// create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;

        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(450, 675));
//            imageView.setLayoutParams(new GridView.LayoutParams(800, 800));
//            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }
        //        moviePosterAddress.add(aTitle.getString(MOVIE_POSTER));
        Picasso.with(mContext).load("http://image.tmdb.org/t/p/w780/" + moviePathString[position])
//                + "&sa=D&ust=1456543435696000&usg="
//                + mContext.getResources().getString(R.string.apiKey))
                .placeholder(R.drawable.comingsoon).into(imageView);
        //        Log.e("full urls", "http://image.tmdb.org/t/p/w780/"+ moviePosters.get(position));
        Log.e("Url: ", "http://image.tmdb.org/t/p/w780/" + moviePathString[position]);
        return imageView;
    }

// added ImageView take it out if it doesn't work! 3/4
    private class RequestPopularMovies extends AsyncTask<ImageView, Void, Void> {
        StringBuilder total = new StringBuilder();
        final String MOVIE_BLOCKS = "results";
        final String MOVIE_TITLE = "original_title";
        //        final String MOVIE_POSTER = "poster_path";
        final String MOVIE_OVERVIEW = "overview";
        final String MOVIE_RELEASE_DATE = "release_date";
        StringBuilder moviesList = new StringBuilder();

        @Override
        protected Void doInBackground(ImageView... params) {
// using the apikey in a separate string file to protect the apikey
            try {
                URL url = new URL("https://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=" + mContext.getResources().getString(R.string.apiKey));
                // making url request and sending it to be read
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                // preparing a reader to go through the response
                BufferedReader r = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                // below allows for controlled reading of potentially large text
                String line;
                while ((line = r.readLine()) != null) {
                    total.append(line);
                }
                // checking if getting back something
                Log.e("Response", total.toString());

                //JSON section
                popArray = new JSONObject(total.toString());
                movies = popArray.getJSONArray(MOVIE_BLOCKS);
                Log.e("movie titles", movies.toString());

                // get movie poster filenames and put in an array
                for (int i = 0; i < movies.length(); i++) {
                    JSONObject aTitle = movies.getJSONObject(i);
                    //could work
//                    moviePathString[i] = aTitle.getString(MOVIE_POSTER);
                    moviePosterAddress.add(aTitle.getString(MOVIE_POSTER));
                    // check to see if working
                    Log.e("poster path" + i, aTitle.getString(MOVIE_POSTER));
                }
                moviePathString = moviePosterAddress.toArray(new String[moviePosterAddress.size()]);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

        }
    }
}