package com.example.android.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

public class MovieParcelable implements Parcelable {
    private Movie movie;

       @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeParcelable(this.movie, flags);
        dest.writeString(movie.getTitle());
        dest.writeString(movie.getPoster());
        dest.writeString(movie.getSynopsis());
        dest.writeString(movie.getReleaseDate());
        dest.writeString(movie.getRating());
    }

    public MovieParcelable(Movie movie) {
        this.movie = movie;
    }

    protected MovieParcelable(Parcel in) {
        this.movie = in.readParcelable(Movie.class.getClassLoader());
    }

    public static final Creator<MovieParcelable> CREATOR = new Creator<MovieParcelable>() {
        public MovieParcelable createFromParcel(Parcel source) {
            return new MovieParcelable(source);
        }

        public MovieParcelable[] newArray(int size) {
            return new MovieParcelable[size];
        }
    };
}
