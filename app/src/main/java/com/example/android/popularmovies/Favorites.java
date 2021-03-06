package com.example.android.popularmovies;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.ContentResolverCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

public class Favorites extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
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
        Cursor cs = getContentResolver().query(MovDBContract.MovieEntry.CONTENT_URI, mProjection, null, null, null);

        StringBuilder sb = new StringBuilder();
        if (cs == null) {
            Log.e("Output:", String.valueOf(cs.getCount()));
        } else {
            cs.moveToFirst();
            while (cs.moveToNext()) {
                Log.w("...Provider...", cs.getString(0) + "-" + cs.getString(1));
                /*
                0 Title
                1 MovieID
                2 Synopsis
                3 Poster
                4 Backdrop
                5 Rating
                6 Release
                7 favorite <--- probably don't neet it
                 */
                sb.append(cs.getString(0))
                        .append(" - ")
                        .append(cs.getString(1))
                        .append("\n - ")
                        .append(cs.getString(2))
                        .append(" - ")
                        // movie poster url is 3
                        .append(cs.getString(3))
                        .append("\n===================\n");
            }
            TextView tt = (TextView) findViewById(R.id.output);
            tt.setText(sb.toString());
        }

    }


}
