package com.example.android.popularmovies;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {
    public Context mContext;
    private String[] moviePathString;

    public ImageAdapter(Context c, ArrayList<String> moviePosterAddress) {
        mContext = c;
        //allows me to take movie info downloaded from MainActivity
        moviePathString = moviePosterAddress.toArray(new String[moviePosterAddress.size()]);
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

}