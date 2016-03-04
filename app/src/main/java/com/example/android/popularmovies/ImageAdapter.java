package com.example.android.popularmovies;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageAdapter extends BaseAdapter {
    public Context mContext;
    private String moviesPosters;


    public ImageAdapter(Context c) {
        mContext = c;
        new RequestPopularMovies().execute(null, null, null);
    }

    @Override
    public int getCount() {
//        return moviesPosters.length();
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    private class RequestPopularMovies extends AsyncTask<Void, Void, Void> {
        StringBuilder total = new StringBuilder();
        String apiKey = mContext.getResources().getString(R.string.apiKey);

        @Override
        protected Void doInBackground(Void... params) {
// using the apikey in a seperate string file to protect the apikey
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


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }


//    private class RequestPopularMovies extends AsyncTask<Void, Void, Void> {
//        StringBuilder total = new StringBuilder();
//
//        @Override
//        protected Void doInBackground(Void... params) {
//            try {
//                // using the apikey in a seperate string file to protect the apikey
//                URL url = new URL("https://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=" + getResources().getString(R.string.apiKey));
//                // making url request and sending it to be read
//                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                // preparing a reader to go through the response
//                BufferedReader r = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//                // below allows for controlled reading of potentially large text
//                String line;
//                while ((line = r.readLine()) != null) {
//                    total.append(line);
//                }
//                // experimental stuff below
//
//                JSONObject popArray = new JSONObject(total.toString());
//                JSONArray movies = popArray.getJSONArray(MOVIE_BLOCKS);
//            }
//            return null;
//        }
//
//    }
}