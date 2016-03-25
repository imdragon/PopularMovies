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
                MovDBContract.MovieEntry.COLUMN_MOVIEID};
        Cursor cs = getContentResolver().query(MovDBContract.MovieEntry.CONTENT_URI, mProjection, null, null, null);

StringBuilder sb = new StringBuilder();
        if (cs == null) {
            Log.e("Output: ", String.valueOf(cs.getCount()));
        } else {
            cs.moveToFirst();
            while (cs.moveToNext()) {
//                Log.w("...Provider...", cs.getString(0) + "-" + cs.getString(1) + "-" + cs.getString(2));
                Log.w("...Provider...", cs.getString(0) + "-"+ cs.getString(1));
                sb.append(cs.getString(0) + " - "+ cs.getString(1)+"\n");
            }
            TextView tt = (TextView) findViewById(R.id.output);
            tt.setText(sb.toString());
        }

    }


}
