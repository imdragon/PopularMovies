package com.example.android.popularmovies;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class getTrailerOrReviews extends AsyncTask<Object, Void, Void> {
    Activity callingActivity;
    StringBuilder total = new StringBuilder();
    String firstTrailer;

    public getTrailerOrReviews(Activity activity) {
        callingActivity = activity;
    }


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
    protected Void doInBackground(Object... params) {
        try {
            URL url = new URL("http://api.themoviedb.org/3/movie/" + params[0] + "/"+params[1]+"?api_key=" + callingActivity.getString(R.string.apiKey));
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

            JSONArray trailers = popArray.getJSONArray("results");
            for (int i = 0; i < trailers.length(); i++) {
                JSONObject trailer = trailers.getJSONObject(i);
                firstTrailer = trailer.getString("key");
                ((MovieDetailsActivity) callingActivity).trailerLink = firstTrailer;
            }
            Log.e("Trail", total.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        TextView mTrailer = (TextView) callingActivity.findViewById(R.id.trailerLink);

        mTrailer.setVisibility(View.VISIBLE);
    }
}